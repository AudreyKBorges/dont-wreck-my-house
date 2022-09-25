package learn.dontwreckmyhouse.data;


import learn.dontwreckmyhouse.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HostRepositoryTestDouble implements HostRepository {

    public final static Host HOST = new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15","Yearnes","eyearnes0@sfgate.com","(806) 1783815","3 Nova Trail","Amarillo","TX","79182", BigDecimal.valueOf(Double.parseDouble(String.valueOf(340))),BigDecimal.valueOf(Double.parseDouble(String.valueOf(425))));

    @Override
    public List<Host> findAll() {
        ArrayList<Host> all = new ArrayList<>();
        all.add(new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15","Yearnes","eyearnes0@sfgate.com","(806) 1783815","3 Nova Trail","Amarillo","TX","79182", BigDecimal.valueOf(Double.parseDouble(String.valueOf(340))),BigDecimal.valueOf(Double.parseDouble(String.valueOf(425)))));
        return all;
    }

    @Override
    public Host findById(String id) {
        for(Host host : findAll()){
            if(Objects.equals(host.getId(), id)){
                return host;
            }
        }
        return null;
    }
}
