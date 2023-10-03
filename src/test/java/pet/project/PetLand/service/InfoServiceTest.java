package pet.project.PetLand.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pet.project.PetLand.model.Info;
import pet.project.PetLand.repository.InfoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InfoServiceTest {
    @InjectMocks
    private InfoService infoService;
    @Mock
    private InfoRepository infoRepositoryMock;


    private List<Info> getInitialTestInfoList() {
        Info info1 = getTestInfo(1L, "правила1", "инструкции1");
        Info info2 = getTestInfo(2L, "правила2", "инструкции2");

        List<Info> infoList = new ArrayList<>();
        infoList.add(info1);
        infoList.add(info2);

        return infoList;
    }
    private Info getTestInfo(long id, String area, String instructions) {
        Info info = new Info();
        info.setId(id);
        info.setArea(area);
        info.setInstructions(instructions);
        return info;
    }
    @Test
    void getAll() {
        List<Info> infoList = getInitialTestInfoList();
        when(infoRepositoryMock.findAll()).thenReturn(infoList);
        List<Info> expected = infoList;
        List<Info> actual = infoService.getAll();
        Assertions.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
    @Test
    void findInfoById() {
        Info info = getTestInfo(1L, "правила1", "инструкции1");
        Info infoWrong = getTestInfo(4L, "правила4", "инструкции4");

        when(infoRepositoryMock.findById(1L)).thenReturn(Optional.of(info));
        when(infoRepositoryMock.findById(4L)).thenReturn(Optional.of(infoWrong));

        Info expected = info;
        Info actual = infoService.findInfoById(1L);

        Assertions.assertThat(actual).isEqualTo(expected);

        Info actualWrong = infoService.findInfoById(4L);
        Assertions.assertThat(actualWrong).isNotEqualTo(expected);
    }
    @Test
    void createInfo() {
        Info info = getTestInfo(1L, "правила1", "инструкции1");
        when(infoRepositoryMock.save(info)).thenReturn(info);

        Info expected = info;
        Info actual = infoService.createInfo(info);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
    @Test
    void updateInfo() {
        Info infoNotFound = getTestInfo(4L, "правила4", "правила4");
        when(infoRepositoryMock.findById(4L)).thenReturn(Optional.empty());
        Info expected = null;
        Info actual = infoService.updateInfo(infoNotFound);
        Assertions.assertThat(actual).isEqualTo(expected);

        Info info = getTestInfo(2L, "правила4", "инструкции4");
        when(infoRepositoryMock.findById(2L)).thenReturn(Optional.of(info));
        when(infoRepositoryMock.save(info)).thenReturn(info);
        expected = info;
        actual = infoService.updateInfo(info);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
    @Test
    void deleteInfoById() {
        when(infoRepositoryMock.findById(4L)).thenReturn(Optional.empty());
        Info expected = null;
        Info actual = infoService.deleteInfoById(4L);
        Assertions.assertThat(actual).isEqualTo(expected);

        Info info = getTestInfo(2L, "правила4", "иструкции4");
        when(infoRepositoryMock.findById(2L)).thenReturn(Optional.of(info));

        expected = info;
        actual = infoService.deleteInfoById(2L);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
    @Test
    void findInfoByArea() {
        Info infoNotFound = getTestInfo(4L, "правила4", "инструкции4");
        when(infoRepositoryMock.findFirstByAreaContainingIgnoreCase("правила4")).thenReturn(Optional.empty());
        Assertions.assertThat(infoService.findInfoByArea("правила4")).isNull();

        Info info = getTestInfo(2L, "правила2", "инструкции2");
        when(infoRepositoryMock.findFirstByAreaContainingIgnoreCase("правила2")).thenReturn(Optional.of(info));
        Assertions.assertThat(infoService.findInfoByArea("правила2")).isEqualTo(info);
    }
}
