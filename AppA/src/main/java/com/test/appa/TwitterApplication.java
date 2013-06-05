package com.test.appa;


import java.util.logging.Logger;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterApplication {
    private final Logger logger = Logger.getLogger(TwitterApplication.class.getName());

    public static void main(String[] args) {
        
    }
  
    public void publish( String message){
    	try {
           Twitter twitter = new TwitterFactory().getInstance();
           try {
               RequestToken requestToken = twitter.getOAuthRequestToken();
               AccessToken accessToken = null;
               while (null == accessToken) {
                   logger.fine("Open the following URL and grant access to your account:");
                   logger.fine(requestToken.getAuthorizationURL());
                   try {
                           accessToken = twitter.getOAuthAccessToken(requestToken);
                   } catch (TwitterException te) {
                       if (401 == te.getStatusCode()) {
                           logger.severe("Unable to get the access token.");
                       } else {
                           te.printStackTrace();
                       }
                   }
               }
               logger.info("Got access token.");
               logger.info("Access token: " + accessToken.getToken());
               logger.info("Access token secret: " + accessToken.getTokenSecret());
           } catch (IllegalStateException ie) {
               // access token is already available, or consumer key/secret is not set.
               if (!twitter.getAuthorization().isEnabled()) {
                   logger.severe("OAuth consumer key/secret is not set.");
                   return;
               }
           }
           Status status = twitter.updateStatus(message);
           logger.info("Successfully updated the status to [" + status.getText() + "].");
       } catch (TwitterException te) {
           te.printStackTrace();
           logger.severe("Failed to get timeline: " + te.getMessage());
       } 
    }
    

}
