package com.editor.xml_editor;

import java.util.LinkedList;

public class NetworkAnalysis {
    private Graph g;

    NetworkAnalysis(Graph g) {
        this.g = g;
    }

    public int getMostInfluencer() {
        // we can't simply loop through all users and find the one with that's following the most users
        // because the user with the most followers might not be following anyone
        // so we need to loop through all users and find the one with the most followers
        int[] followers = new int[g.getV()];
        for(int i = 0; i < g.getV(); i++) {
            LinkedList<Integer> adj = g.getAdj(i);
            for(Integer follower : adj) {
                followers[follower]++;
            }
        }

        // now we have an array of followers, we can loop through it and find the user with the most followers
        int mostInfluencer = -1;
        int maxFollowers = 0;
        for(int i = 0; i < followers.length; i++) {
            if(followers[i] > maxFollowers) {
                maxFollowers = followers[i];
                mostInfluencer = i;
            }
        }
        
        return mostInfluencer;
    }

    // who is the most active user (connected to lots of users)
    public int getMostActive() {
        // loop through all users and find the one with the most connections
        int mostActive = -1;
        int maxConnections = 0;

        for(int i = 0; i < g.getV(); i++) {
            LinkedList<Integer> adj = g.getAdj(i);
            // count the number of connections
            int connections = adj.size();
            if(connections > maxConnections) {
                maxConnections = connections;
                mostActive = i;
            }
        }

        return mostActive;
    }

    // the mutual followers between 2 users
    public LinkedList<Integer> getMutualFollowers(int u1, int u2) {
        // loop through all followers of u1 and check if they are also followers of u2
        int[] followersOfU1 = new int[g.getV()];
        int[] followersOfU2 = new int[g.getV()];
        LinkedList<Integer> mutualFollowers = new LinkedList<Integer>();

        LinkedList<Integer> adjOfU1 = g.getAdj(u1);
        LinkedList<Integer> adjOfU2 = g.getAdj(u2);

        for(Integer follower : adjOfU1) {
            followersOfU1[follower] = 1;
        }
        for(Integer follower : adjOfU2) {
            followersOfU2[follower] = 1;
        }

        for(int i = 0; i < g.getV(); i++) {
            if(followersOfU1[i] == 1 && followersOfU2[i] == 1) {
                mutualFollowers.add(i);
            }
        }

        return mutualFollowers;
    }

    // for each user, suggest a list of users to follow (the followers of his followers)
    public LinkedList<Integer> getSuggestFollowers(int u) {
        // loop through all followers of u and find their followers
        LinkedList<Integer> followers = g.getAdj(u);
        LinkedList<Integer> suggestedFollowers = new LinkedList<Integer>();

        for(Integer follower : followers) {
            LinkedList<Integer> followersOfFollower = g.getAdj(follower);
            for(Integer followerOfFollower : followersOfFollower) {
                if(!suggestedFollowers.contains(followerOfFollower) && followerOfFollower != u) {
                    suggestedFollowers.add(followerOfFollower);
                }
            }
        }

        return suggestedFollowers;
    }
}
