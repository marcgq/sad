//
//  Client.swift
//  SadChat
//
//  Created by Marc on 7/11/21.
//

import Combine
import Foundation
import SwiftUI

final class ChatStore: ObservableObject, SocketDelegate {
    @Published private(set) var messages: [Message] = []
    @Published public var usernameInUse = false
    private var username = ""
    private var socket = SocketServer() // 1
    
    
    func addMessage(message: Message){
        messages.append(message)
    }
    
    func socketDataReceived(result: String) {
        var components = result.split(separator:">", maxSplits: 1)
        components[1].removeFirst()
        let newMessage: Message = Message(nick: String(components[0]), message: String(components[1]), isMine: false)
        messages.append(newMessage)
    }
    
    func usernameTaken() {
        usernameInUse = true;
    }
    
    
    // MARK: - Connection
    func connect(username: String){ // 2
        self.username = username
        usernameInUse = false
        socket.delegate = self
        socket.connect()
        socket.writeToOutputStream(string: self.username+"\n")
    }

    
    func send(text: String) {
        socket.writeToOutputStream(string: text+"\n")
        let m: Message = Message(nick: username, message: text, isMine: true)
        addMessage(message: m)
       
    }

    
    func disconnect(){
        socket.closeNetworkConnection()
    }
    
}
