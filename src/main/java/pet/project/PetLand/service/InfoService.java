package pet.project.PetLand.service;

import org.springframework.stereotype.Service;
import pet.project.PetLand.model.Info;
import pet.project.PetLand.repository.InfoRepository;

import java.util.List;

@Service
public class InfoService {
    private final InfoRepository infoRepository;

    public InfoService(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    public List<Info> getAll() {
        return infoRepository.findAll();
    }

    public Info findInfoById(long id) {
        return infoRepository.findById(id).orElse(null);
    }

    public Info createInfo(Info info) {
        return infoRepository.save(info);
    }

    public Info updateInfo(Info info) {
        if (findInfoById(info.getId()) == null) {
            return null;
        } else {
            return infoRepository.save(info);
        }
    }

    public Info deleteInfoById(Long id) {
        Info info = findInfoById(id);
        if (info == null) {
            return null;
        } else {
            infoRepository.deleteById(id);
            return info;
        }
    }

    public Info findInfoByArea(String area) {
        return infoRepository.findFirstByAreaContainingIgnoreCase(area).orElse(null);
    }

    public List<Info> findAll() {
        return infoRepository.findAll();
    }
}
