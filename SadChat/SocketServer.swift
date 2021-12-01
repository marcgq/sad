//
//  SocketServer.swift
//  SadChat
//
//  Created by Marc on 7/11/21.
//

import Foundation
class SocketServer: NSObject{
    
    static private let HOST = "sadchat.ddns.net"
    static private let PORT: UInt32 = 50000
    
    //Server action code
    static public let SOME_ACTION = 100

    private var inputStream: InputStream!
    private var outputStream: OutputStream!
    
    public var isOpen: Bool = false
    weak var delegate: SocketDelegate?
    
    override init(){
        super.init()
    }
    
    //1
    public func connect(){
        //set up uninitialized socket streams without automatic memory management
        var readStream: Unmanaged<CFReadStream>?
        var writeStream: Unmanaged<CFWriteStream>?
        
        //bind read and write socket streams together and connect them to the socket of the host
        CFStreamCreatePairWithSocketToHost(
                             kCFAllocatorDefault,
                             SocketServer.HOST as CFString,
                             SocketServer.PORT,
                             &readStream,
                             &writeStream)
        
        
        //store retained references
        inputStream = readStream!.takeRetainedValue()
        outputStream = writeStream!.takeRetainedValue()
        
        inputStream.delegate = self
        //run a loop
        inputStream.schedule(in: .current, forMode: .common)
        outputStream.schedule(in: .current, forMode: .common)

        //open flood gates
        inputStream.open()
        outputStream.open()
        
        isOpen = true
    }
    
    //2
    public func closeNetworkConnection(){
        inputStream.close()
        outputStream.close()
        isOpen = false
    }
    
    //3
    private func serverRequest(serverActionCode: UInt8, int: UInt8){
        writeToOutputStream(int: serverActionCode)
        writeToOutputStream(int: int)
    }
    
    //4
    public func someAction(someParam: UInt8!){
        serverRequest(serverActionCode: UInt8(SocketServer.SOME_ACTION), int: someParam)
    }
}

extension SocketServer: StreamDelegate{
    
    //Delegate method override
    func stream(_ aStream: Stream, handle eventCode: Stream.Event){
        switch eventCode{
        case .hasBytesAvailable:
            let s = self.readLine(stream: aStream as! InputStream)
            if s=="\0" {
                closeNetworkConnection()
            }else if s == "Username already taken"{
                self.delegate?.usernameTaken()
            }else if s != ""{
                self.delegate?.socketDataReceived(result: s!)
            }
        case .openCompleted:
            isOpen = true
        default:
            print("StreamDelegate event")
        }
    }
    
    //1
    private func getBufferFrom(stream: InputStream, size: Int) -> UnsafeMutablePointer<UInt8> {
        let buffer = UnsafeMutablePointer<UInt8>.allocate(capacity: size)
        
        while (stream.hasBytesAvailable) {
            let numberOfBytesRead = self.inputStream.read(buffer, maxLength: size)
            if numberOfBytesRead < 0, let error = stream.streamError {
                print(error)
                break
            }
            if numberOfBytesRead == 0{
                //EOF
                break
            }
        }
        return buffer
    }
    
    private func readLine(stream: InputStream) -> String?{
            let buffer = UnsafeMutablePointer<UInt8>.allocate(capacity: 1)
            var lineBytes = Data()
            var byteRead = Data()
            while (stream.hasBytesAvailable) {
                self.inputStream.read(buffer, maxLength: 1)
                byteRead = Data(bytes:buffer, count: 1)
                if Character(UnicodeScalar(byteRead[0])).isNewline{
                    break
                } else if byteRead[0]|0b01111111 == 0b01111111{
                    lineBytes += byteRead
                } else if byteRead[0]|0b00011111 == 0b11011111{
                    self.inputStream.read(buffer, maxLength: 1)
                    lineBytes += byteRead + Data(bytes:buffer, count: 1)
                } else if byteRead[0]|0b00001111 == 0b11101111{
                    self.inputStream.read(buffer, maxLength: 2)
                    lineBytes += byteRead + Data(bytes:buffer, count: 2)
                } else if byteRead[0]|0b00000111 == 0b11110111{
                    self.inputStream.read(buffer, maxLength: 3)
                    lineBytes += byteRead + Data(bytes:buffer, count: 3)
                }
            }
        return String(data: lineBytes, encoding: .utf8)
    }
    
    
    //2
    private func readDataFrom(stream: InputStream, size: Int) -> Data?{
        let buffer = getBufferFrom(stream: stream, size: size)
        return Data(bytes: buffer, count: size)
    }
    
    //3
    private func readStringFrom(stream: InputStream, withSize: Int) -> String?{
        let d = readDataFrom(stream: stream, size: withSize)!
        return String(data: d, encoding: .utf8)
    }
    
    //4
    private func readStringFrom(stream: InputStream) -> String?{
        let len: Int = Int(Int32(readIntFrom(stream: inputStream)!))
        return readStringFrom(stream: stream, withSize: len)
    }
    
    //5
    private func readIntFrom(stream: InputStream) -> UInt32?{
        let buffer = getBufferFrom(stream: stream, size: 4)
        var int: UInt32 = 0
        let data = NSData(bytes: buffer, length: 4)
        data.getBytes(&int, length: 4)
        int = UInt32(bigEndian: int)
        buffer.deallocate()
        return int
    }
    
    //6
    public func writeToOutputStream(string: String){
        let data = string.data(using: .utf8)!
        data.withUnsafeBytes {
            guard let pointer = $0.baseAddress?.assumingMemoryBound(to: UInt8.self)
            else {
                print("Error joining chat")
                return
            }
            outputStream.write(pointer, maxLength: data.count)
        }
    }
    
    //7
    private func writeToOutputStream(int: UInt8){
        let data = int.description.data(using: String.Encoding.utf8)
        data!.withUnsafeBytes {
            guard let pointer = $0.baseAddress?.assumingMemoryBound(to: UInt8.self)
            else {
                print("Error joining chat")
                return
            }
            outputStream.write(pointer, maxLength: data!.count)
        }
    }
}
