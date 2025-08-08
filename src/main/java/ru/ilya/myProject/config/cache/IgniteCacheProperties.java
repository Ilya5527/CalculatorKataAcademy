package ru.ilya.myProject.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app.cache.ignite")
public class IgniteCacheProperties {
    private boolean enabled = true;
    private String clusterName = "ignite-cluster";
    private List<String> addresses = new ArrayList<>();
    private List<String> cacheNames = new ArrayList<>();
    private int discoveryJoinTimeoutMs = 3000;
    private int networkTimeoutMs = 5000;
    private int failureDetectionTimeoutMs = 5000;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getClusterName() { return clusterName; }
    public void setClusterName(String clusterName) { this.clusterName = clusterName; }

    public List<String> getAddresses() { return addresses; }
    public void setAddresses(List<String> addresses) { this.addresses = addresses; }

    public List<String> getCacheNames() { return cacheNames; }
    public void setCacheNames(List<String> cacheNames) { this.cacheNames = cacheNames; }

    public int getDiscoveryJoinTimeoutMs() { return discoveryJoinTimeoutMs; }
    public void setDiscoveryJoinTimeoutMs(int discoveryJoinTimeoutMs) { this.discoveryJoinTimeoutMs = discoveryJoinTimeoutMs; }

    public int getNetworkTimeoutMs() { return networkTimeoutMs; }
    public void setNetworkTimeoutMs(int networkTimeoutMs) { this.networkTimeoutMs = networkTimeoutMs; }

    public int getFailureDetectionTimeoutMs() { return failureDetectionTimeoutMs; }
    public void setFailureDetectionTimeoutMs(int failureDetectionTimeoutMs) { this.failureDetectionTimeoutMs = failureDetectionTimeoutMs; }
}