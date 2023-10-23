package com.banzzac.message.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {
    @Id
    @Field
    private String name;

    @Field
    private int age;
}
