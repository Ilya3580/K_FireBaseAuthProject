package com.example.firebaseauthprojectk

class UserParametrs {

    private var nameClass:String
    private var surnameClass:String
    private var emailClass:String
    private var passwordClass:String
    constructor(name:String, surname:String, email:String, password:String)
    {
        nameClass = name
        surnameClass = surname
        emailClass = email
        passwordClass = password
    }
    var name:String
        get(){
            return nameClass
        }
        set(value) {
            nameClass = value
        }
    var surname:String
        get(){
            return surnameClass
        }
        set(value) {
            surnameClass = value
        }
    var email:String
        get(){
            return emailClass
        }
        set(value) {
            emailClass = value
        }
    var password:String
        get(){
            return passwordClass
        }
        set(value) {
            passwordClass = value
        }

}