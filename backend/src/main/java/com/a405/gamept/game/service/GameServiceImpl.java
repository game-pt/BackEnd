package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.response.DiceGetResponseDto;
import com.a405.gamept.game.dto.response.RaceGetResponseDto;
import com.a405.gamept.game.entity.Race;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.repository.RaceRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.global.error.enums.ErrorMessage;
import com.a405.gamept.global.error.exception.custom.BusinessException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.play.repository.PlayerRedisRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private final Validator validator;
    Set<ConstraintViolation<Object>> violations;
    private final GameRedisRepository gameRedisRepository;
    private final PlayerRedisRepository playerRedisRepository;

    @Autowired
    public GameServiceImpl(GameRedisRepository gameRedisRepository, PlayerRedisRepository playerRedisRepository) {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.gameRedisRepository = gameRedisRepository;
        this.playerRedisRepository = playerRedisRepository;
    }
    @Override
    public DiceGetResponseDto rollOfDice(DiceGetCommandDto diceGetCommandDto) {
        // 랜덤 객체 생성
        Random random = new Random();

        // 1에서 6까지의 랜덤 숫자 생성
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;

        DiceGetResponseDto diseResult = DiceGetResponseDto.of(dice1, dice2);

        violations = validator.validate(diseResult);

        if (!violations.isEmpty()) {  // 유효성 검사 실패 시
            for (ConstraintViolation<Object> violation : violations) {
                log.error("DiceInvalidException: { GameService " + violation.getMessage() + " }");
            }
            throw new BusinessException(ErrorMessage.DICE_NOT_FOUND);
        }

        return DiceGetResponseDto.of(dice1, dice2);
    }

    @Override
    public boolean gameCheck(String gameCode, String playerCode){
        Game game = gameRedisRepository.findById(gameCode)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(playerCode)
                .orElseThrow(() -> new BusinessException(ErrorMessage.PLAYER_NOT_FOUND));

        List<Player> playerList = game.getPlayerList();
        boolean pass = false;
        for(Player p : playerList){
            if (p.getCode().equals(playerCode)) {
                pass = true;
                break;
            }
        }

        return pass;
    }
}
