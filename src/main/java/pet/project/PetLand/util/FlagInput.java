package pet.project.PetLand.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.Flag;
import pet.project.PetLand.handler.ManualInputHandler;
@Service
public class FlagInput {
    private final static Logger LOGGER = LoggerFactory.getLogger(FlagInput.class);

    private static Flag flag = Flag.None;

    public Flag flag() {
        LOGGER.info("Was invoked method to get flag");
        LOGGER.debug(String.valueOf(flag));
        return flag;
    }

    public void flagNone() {
        LOGGER.info("Was invoked method to change flag to None");
        flag = Flag.None;
    }

    public void flagCustomer() {
        LOGGER.info("Was invoked method to change flag to Customer");
        flag = Flag.Customer;
    }

    public void flagReport() {
        LOGGER.info("Was invoked method to change flag report to Report");
        flag = Flag.Report;
    }

}
