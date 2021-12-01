//
//  Models.swift
//  SadChat
//
//  Created by Marc on 9/11/21.
//

import Foundation
struct Message: Identifiable{
    var id = UUID()
    let nick: String // 4
    let message: String // 5
    let date = Date()
    let isMine: Bool
}
