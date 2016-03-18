/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
/**
 *
 * @author c0564747
 */
@ApplicationScoped
@ManagedBean
public class PostController {
    private List<post> posts;
    private post currentPost;
    
    public PostController(){
        currentPost = new post(-1, -1, "", null, "");
        updatePostFromDatabase();
    }

    public List<post> getPosts() {
        return posts;
    }
    
    public post getCurrentPost(){
        return currentPost;
    }
    public post getPostById(int id){
        post ret = null;
        for(post p : posts){
            if(p.getId() == id){
                return ret;
            }
        }
        return ret;
    }
    
    public post getPostByTitle(String title){
        post ret = null;
        for(post p : posts){
            if(p.getTitle().equals(title)){
                return ret;
            }
        }
        return ret;
    }
    
    public String viewPost(post post){
        currentPost = post;
        return "viewPost";
    }
    
    public String addPost(){
        currentPost = new post(-1,-1, "", null, "");
        return "editPost";
    }
    
    public String editPost(){
        return "editPost";
    }
    
    public String cancelPost(){
        int id = currentPost.getId();
        updatePostFromDatabase();
        currentPost = getPostById(id);
        return "viewPost";
    }
    
    public String deletePost(User user){
        try(Connection conn = Utils.getConnection()) {
           String sql = "DELETE * FROM posts WHERE id = ?";
           PreparedStatement pstmt = conn.prepareStatement(sql);
           pstmt.setInt(1, currentPost.getId()); 
           pstmt.executeUpdate();
            } catch (SQLException ex) {
            Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
        }
        updatePostFromDatabase();
        return "viewPost";
    }
   
    public String savePost(User user){
        try(Connection conn = Utils.getConnection()) {
            if(currentPost.getId() >= 0){
            String sql = "UPDATE posts SET title = ?, contents = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentPost.getTitle());
            pstmt.setString(2, currentPost.getContents());
            pstmt.setInt(3, currentPost.getId());
            pstmt.executeUpdate();
            }else{
              String sql = "INSERT INTO posts (user_id, title, created_time, contents) VALUES (?,?,NOW(),?)";  
              PreparedStatement pstmt = conn.prepareStatement(sql);
              pstmt.setString(1, currentPost.getTitle());
              pstmt.setString(2, currentPost.getContents());
              pstmt.setInt(3, currentPost.getId());
              pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
        }
        updatePostFromDatabase();
        return "viewPost";
    }
    private void updatePostFromDatabase(){
        try {
            //Make a Connection
            posts = new ArrayList<>();
            Connection conn = Utils.getConnection();
            //Build a Query
            String sql = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Parse the Results
            while (rs.next()){
                post p = new post(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                rs.getTimestamp("created_time"),
                rs.getString("contents")
                );
                posts.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

