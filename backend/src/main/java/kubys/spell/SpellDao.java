package kubys.spell;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpellDao extends CrudRepository<Spell, String> {
    List<Spell> findAll();
}
