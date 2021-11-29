//
//  UserInfo.swift
//  SadChat
//
//  Created by Marc on 11/11/21.
//
import Combine
import Foundation

class UserInfo: ObservableObject {
    let userID = UUID()
    @Published var username = ""
}
