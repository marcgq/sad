//
//  ChatScreen.swift
//  SadChat
//
//  Created by Marc on 7/11/21.
//

import Foundation
import SwiftUI

struct ChatScreen: View {
    @StateObject private var store = ChatStore()
    @State private var myMessage = ""
    @EnvironmentObject private var userInfo: UserInfo
    @Environment(\.presentationMode) var mode: Binding<PresentationMode>
    @GestureState private var dragOffset = CGSize.zero
    
    
    private func onAppear(){
        DispatchQueue.main.async{
            store.connect(username: userInfo.username)
        }
    }
    private func onDisappear(){
        store.disconnect()
    }
    private func onCommit(){
        if !myMessage.isEmpty {
            store.send(text: myMessage)
            myMessage=""
        }
        
    }
    private func scrollToLastMessage(proxy: ScrollViewProxy) {
        if let lastMessage = store.messages.last { // 4
            withAnimation(.easeOut(duration: 0.4)) {
                proxy.scrollTo(lastMessage.id, anchor: .bottom) // 5
            }
        }
    }
    
    var body: some View {
        VStack {
            ScrollView {
                ScrollViewReader { proxy in // 1
                    LazyVStack(spacing: 8) {
                        ForEach(store.messages) { message in
                            ChatMessageRow(message: message, isUser: message.isMine).id(message.id)
                            
                        }
                    }
                    .onChange(of: store.messages.count) { _ in // 3
                        scrollToLastMessage(proxy: proxy)
                    }
                    
                }
            }.padding()
            
            HStack {
                TextField("Message", text: $myMessage, onEditingChanged: { _ in }, onCommit: onCommit)
                    .padding(10)
                    .background(Color.secondary.opacity(0.2))
                    .cornerRadius(50)
                
                Button(action: onCommit) {
                    Image(systemName: "arrowshape.turn.up.right")
                        .font(.system(size: 25))
                        .tint(.orange)
                }
                .padding()
                .disabled(myMessage.isEmpty) // 4
            }
            .padding()
        }
        .alert("Nickname \"" + userInfo.username + "\" is already in use", isPresented: $store.usernameInUse) {
            Button("OK", role: .cancel) {
                self.mode.wrappedValue.dismiss()
                store.disconnect()
            }
        } message: {
            Text("Please try joining with a different nickname")
        }
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading: Button(action : {
            self.mode.wrappedValue.dismiss()
            store.disconnect()
        }){
            Image(systemName: "arrow.left")
            Text("Leave chat")
        }.tint(.orange))
        .navigationBarTitle("SadChat")
        .onAppear(perform: onAppear)
        .onDisappear(perform: onDisappear)
        .gesture(DragGesture().updating($dragOffset, body: { (value, state, transaction) in
            
            if(value.startLocation.x < 20 && value.translation.width > 100) {
                self.mode.wrappedValue.dismiss()
            }
            
        }))
        
        
    }
}

