//package com.easywin.notificationservice.config;
//
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotEmpty;
//
////@EnableConfigurationProperties
//public class KafkaConsumerProperties {
//
//    @NotEmpty
//    private String bootstrapServer;
//    private String securityProtocol;
//    private Topics topics;
//    @Min(1)
//    private Integer concurrentConsumers;
//    private String groupId;
//
//    public String getBootstrapServer() {
//        return bootstrapServer;
//    }
//
//    public Topics getTopics() {
//        return topics;
//    }
//
//    public void setTopics(Topics topics) {
//        this.topics = topics;
//    }
//
//    public void setBootstrapServer(String bootstrapServer) {
//        this.bootstrapServer = bootstrapServer;
//    }
//
//    public String getSecurityProtocol() {
//        return securityProtocol;
//    }
//
//    public void setSecurityProtocol(String securityProtocol) {
//        this.securityProtocol = securityProtocol;
//    }
//
//    public Integer getConcurrentConsumers() {
//        return concurrentConsumers;
//    }
//
//    public void setConcurrentConsumers(Integer concurrentConsumers) {
//        this.concurrentConsumers = concurrentConsumers;
//    }
//
//    public String getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(String groupId) {
//        this.groupId = groupId;
//    }
//
//    public static class Topics {
//
//        private String create;
//
//        public String getCreate() {
//            return create;
//        }
//
//        public void setCreate(String create) {
//            this.create = create;
//        }
//    }
//}