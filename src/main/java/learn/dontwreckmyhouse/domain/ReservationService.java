package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.models.Reservation;

import java.util.Objects;

public class ReservationService {
    public Result add(Reservation reservation) throws DataException {
        Result result = validate(reservation);
        if(!result.isSuccess()) {
            return result;
        }
        for(Reservation r : findAll()) {
            if(Objects.equals(reservation.getSection(), r.getSection())) {
                if(Objects.equals(reservation.getRow(), r.getRow())) {
                    if(Objects.equals(reservation.getColumn(), r.getColumn())) {
                        result.addMessage("Cannot create duplicate panel");
                    }
                }
            }
        }

        if(panel != null && panel.getId() > 0){
            result.addMessage("Cannot create existing panel");
        }

        if(result.isSuccess()){
            panel = repository.add(panel);
            result.setPanel(panel);
        }
        return result;
    }
}
