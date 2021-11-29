//
//  SocketDelegate.swift
//  SadChat
//
//  Created by Marc on 7/11/21.
//

import Foundation
import SwiftUI

protocol SocketDelegate: AnyObject{
    //1
    func socketDataReceived(result: String)
    
    //2
    func usernameTaken()
}

