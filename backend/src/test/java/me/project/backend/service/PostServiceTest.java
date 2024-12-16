package me.project.backend.service;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    void test() {
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(2);
        integers.add(3);

        List<Character> characters = new ArrayList<Character>();
        modelMapper.map(integers, characters);
        System.out.println(characters);
    }

}