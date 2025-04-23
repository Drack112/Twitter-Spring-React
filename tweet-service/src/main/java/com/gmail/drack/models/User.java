package com.gmail.drack.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "about")
    private String about;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "private_profile", nullable = false, columnDefinition = "boolean default false")
    private boolean privateProfile;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default false")
    private boolean active;

    @Column(name = "muted_direct_messages", nullable = false, columnDefinition = "boolean default false")
    private boolean mutedDirectMessages;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pinned_tweet_id")
    private Tweet pinnedTweet;

    @ManyToMany
    @JoinTable(name = "user_blocked", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "blocked_user_id"))
    private Set<User> userBlockedList = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_muted", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "muted_user_id"))
    private Set<User> userMutedList = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_subscriptions", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private Set<User> followers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_subscriptions", joinColumns = @JoinColumn(name = "subscriber_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> following = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_follower_requests", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<User> followerRequests = new HashSet<>();
}
