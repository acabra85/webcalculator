package com.acabra.mmind.core;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MMindPlayerTest {
    @Test
    public void mustReturnF4S0() {
        String secret = "1234";
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, secret.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(4);
        Assertions.assertThat(actual.spikes).isEqualTo(0);
    }

    @Test
    public void mustReturnF3S0() {
        String secret = "1234";
        String _guess = "1235";
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(3);
        Assertions.assertThat(actual.spikes).isEqualTo(0);
    }

    @Test
    public void mustReturnF2S0() {
        String secret = "1234";
        String _guess = "1285";
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(2);
        Assertions.assertThat(actual.spikes).isEqualTo(0);
    }

    @Test
    public void mustReturnF1S0() {
        String secret = "1234";
        String _guess = "1000";
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(1);
        Assertions.assertThat(actual.spikes).isEqualTo(0);
    }

    @Test
    public void mustReturnF0S0() {
        String secret = "1234";
        String _guess = "7896";
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(0);
        Assertions.assertThat(actual.spikes).isEqualTo(0);
    }

    @Test
    public void mustReturnF0S1() {
        String secret = "1234";
        String _guess = "7396";
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(0);
        Assertions.assertThat(actual.spikes).isEqualTo(1);
    }

    @Test
    public void mustReturnF0S2() {
        String secret = "1234";
        String _guess = "7391";
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(0);
        Assertions.assertThat(actual.spikes).isEqualTo(2);
    }

    @Test
    public void mustReturnF0S3() {
        String secret = "1234";
        String _guess = "4391";
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(0);
        Assertions.assertThat(actual.spikes).isEqualTo(3);
    }

    @Test
    public void mustReturnF0S4() {
        String secret = "1234";
        String _guess = "4321";
        int expectedFixes = 0;
        int expectedSpikes = 4;
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(expectedFixes);
        Assertions.assertThat(actual.spikes).isEqualTo(expectedSpikes);
    }

    @Test
    public void mustReturnF1S3() {
        String secret = "1234";
        String _guess = "1423";
        int expectedFixes = 1;
        int expectedSpikes = 3;
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(expectedFixes);
        Assertions.assertThat(actual.spikes).isEqualTo(expectedSpikes);
    }

    @Test
    public void mustReturnF1S2() {
        String secret = "1234";
        String _guess = "1403";
        int expectedFixes = 1;
        int expectedSpikes = 2;
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(expectedFixes);
        Assertions.assertThat(actual.spikes).isEqualTo(expectedSpikes);
    }

    @Test
    public void mustReturnF1S1() {
        String secret = "1234";
        String _guess = "1003";
        int expectedFixes = 1;
        int expectedSpikes = 1;
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(expectedFixes);
        Assertions.assertThat(actual.spikes).isEqualTo(expectedSpikes);
    }

    @Test
    public void mustReturnF2S1() {
        String secret = "1234";
        String _guess = "1203";
        int expectedFixes = 2;
        int expectedSpikes = 1;
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(expectedFixes);
        Assertions.assertThat(actual.spikes).isEqualTo(expectedSpikes);
    }

    @Test
    public void mustReturnF2S2() {
        String secret = "1234";
        String _guess = "1243";
        int expectedFixes = 2;
        int expectedSpikes = 2;
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, "token");

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());
        Assertions.assertThat(actual.fixes).isEqualTo(expectedFixes);
        Assertions.assertThat(actual.spikes).isEqualTo(expectedSpikes);
    }

    @Test
    public void mustReturnNewPlayer() {
        String secret = "1234";
        String _guess = "4545";
        int expectedFixes = 0;
        int expectedSpikes = 4;
        String newSecret = "5454";
        String token = "token";
        MMindPlayer underTest = new MMindPlayer(1L, "player", secret, token).newSecret(newSecret);

        MMindMoveResult actual = underTest.respond(0L, 0, _guess.toCharArray());

        Assertions.assertThat(underTest.getToken()).isEqualTo(token);
        Assertions.assertThat(underTest.secret).isNotEqualTo(secret.toCharArray());
        Assertions.assertThat(underTest.secret).isEqualTo(newSecret.toCharArray());
        Assertions.assertThat(underTest.move()).isEqualTo(0L);
        Assertions.assertThat(underTest.move()).isEqualTo(1L);
        Assertions.assertThat(actual.fixes).isEqualTo(expectedFixes);
        Assertions.assertThat(actual.spikes).isEqualTo(expectedSpikes);
    }
}