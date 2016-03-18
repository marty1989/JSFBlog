/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author bmart
 */
@ManagedBean
@SessionScoped
public class Login {
    private String username;
    private String password;
    private boolean loggedIn;
    private User currentUser;

    public Login() {
        username = null;
        password = null;
        loggedIn = false;
        currentUser = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public String login() throws UnsupportedEncodingException, NoSuchAlgorithmException{
        String passhash = Utils.hash(password);
        for(User u : UserController.getInstance().getUsers()){
            if(username.equals(u.getUsername()) && passhash.equals(u.getPasshash())){
                loggedIn = true;
                currentUser = u;
                return "index";
            }
        }
        currentUser = null;
        loggedIn = false;
        return "index";
    }
}
