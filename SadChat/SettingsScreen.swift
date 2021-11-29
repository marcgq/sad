//
//  SettingsScreen.swift
//  SadChat
//
//  Created by Marc on 11/11/21.
//

import Foundation
import SwiftUI

struct SettingsScreen: View {
    @EnvironmentObject private var userInfo: UserInfo
    
    private var isUsernameValid: Bool {
        !userInfo.username.trimmingCharacters(in: .whitespaces).isEmpty
    }
    
    let background = LinearGradient(gradient: Gradient(colors: [Color(red: 0.98, green: 0.52, blue: 0.00), Color(red: 1.00, green: 0.74, blue: 0.11)]), startPoint: .bottom, endPoint: .top)
    
    var body: some View {
        ZStack{
            background.ignoresSafeArea()
            VStack{
                Spacer()
                Text("by Marc Garcia and Raquel Abad")
                    .foregroundColor(Color(UIColor.systemBackground))
                    .padding()
            }.ignoresSafeArea(.keyboard, edges: .bottom)
            VStack{
                Spacer()
                Image("Logo")
                Text("Input a nickname and click Join to start chatting!")
                    .foregroundColor(Color(UIColor.systemBackground))
                TextField("Nickname", text: $userInfo.username)
                    .padding(.horizontal, 20)
                    .font(.title3)
                NavigationLink("Join", destination: ChatScreen())
                    .disabled(!isUsernameValid)
                    .padding(.horizontal, 30)
                    .padding(.vertical, 20)
                    .background(Color(UIColor.systemBackground))
                    .clipShape(Capsule())
                    .font(.title3)
                    .tint(.orange)
                Spacer()
            }
        }
        .navigationBarHidden(true)
        .navigationTitle("Unjoin")
        .textFieldStyle(.roundedBorder)
    }
}

