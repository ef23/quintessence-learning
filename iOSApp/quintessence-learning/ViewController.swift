//
//  ViewController.swift
//  quintessence-learning
//
//  Created by Eric Feng on 7/18/17.
//  Copyright © 2017 Eric Feng. All rights reserved.
//

import UIKit
import FirebaseAuth
import FirebaseDatabase
class ViewController: UIViewController {

    var ref : Database?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        ref = Database.database()
        //redirects a logged in user to the appropriate view
        Auth.auth().addStateDidChangeListener() { auth, user in
            if user != nil {
                self.ref!.reference().child(Common.USER_PATH).child(user!.uid).observe(.value, with: { (snapshot) in
                    let value = snapshot.value as? NSDictionary
                    let userType = value?["Type"] as? String ?? ""
                    
                    if (userType == "Admin"){
                        let adminViewController = self.storyboard?.instantiateViewController(withIdentifier: "Admin") as! UINavigationController
                        self.present(adminViewController, animated: true)
                    } else if (userType == "User") {
                        let userViewController = self.storyboard?.instantiateViewController(withIdentifier: "User") as! UITabBarController
                        self.present(userViewController, animated: true)
                    } else {
                        Server.showError(message: "Could not load user profile!")
                    }
                }) { (error) in
                    Server.showError(message: error.localizedDescription)
                }
            }
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}
