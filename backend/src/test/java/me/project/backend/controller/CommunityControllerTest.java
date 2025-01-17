package me.project.backend.controller;

import me.project.backend.domain.Community;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

@SpringBootTest
class CommunityControllerTest {

    @Autowired
    private CommunityController communityController;


    @Test
    void addCommunity() {
        Community community = new Community();
        community.setName("test");
        community.setDescription("test");
        Community added = communityController.save(community);
        assertThat(added).isNotNull();
        System.out.println(added);
    }

}