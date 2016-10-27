package com.solidify.oauth2.security;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTransformerTest {

    UserTransformer transformer = new UserTransformer();

    @Test
    public void shouldTransformModelToDto(){
        // given
        User input = new User();
        input.setEnabled(Boolean.TRUE);
        input.setEmail("kno@gmail.com");
        input.setLastName("Iron");
        input.setFirstName("Man");
        input.setId(1L);

        // when
        UserDto dto = transformer.apply(input);

        // then
        assertEquals(input.getId(), dto.getId());
        assertEquals(input.getEmail(), dto.getEmail());
        assertEquals(input.getFirstName(), dto.getFirstName());
        assertEquals(input.getLastName(), dto.getLastName());
        assertEquals(input.getEnabled(), dto.isEnabled());
    }

}
