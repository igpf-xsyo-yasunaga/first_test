package org.springframework.samples.petclinic.owner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * シンプルなPetController用のテストクラス
 */
@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    private static final int TEST_OWNER_ID = 1;
    private static final int TEST_PET_ID = 1;

    @Mock
    private OwnerRepository owners;

    @InjectMocks
    private PetController petController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // FormattingConversionServiceを作成してPetTypeFormatterを登録
        FormattingConversionService conversionService = new FormattingConversionService();
        conversionService.addFormatter(new PetTypeFormatter(owners));
        
        // MockMvcをセットアップ
        mockMvc = MockMvcBuilders.standaloneSetup(petController)
                .setConversionService(conversionService)
                .build();

        // テストデータ
        PetType cat = new PetType();
        cat.setId(1);
        cat.setName("cat");
        
        // Mockの設定
        when(owners.findPetTypes()).thenReturn(List.of(cat));
        
        Owner owner = new Owner();
        owner.setId(TEST_OWNER_ID);
        owner.setFirstName("Test");
        owner.setLastName("Owner");
        
        Pet pet = new Pet();
        pet.setId(TEST_PET_ID);
        pet.setName("TestPet");
        pet.setType(cat);
        pet.setBirthDate(LocalDate.now().minusYears(1));
        
        owner.addPet(pet);
        
        when(owners.findById(TEST_OWNER_ID)).thenReturn(Optional.of(owner));
    }

    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createOrUpdatePetForm"))
            .andExpect(model().attributeExists("pet"));
    }

    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
                .param("name", "NewPet")
                .param("type", "cat")
                .param("birthDate", "2023-04-01"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    void testInitUpdateForm() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("pet"))
            .andExpect(view().name("pets/createOrUpdatePetForm"));
    }
}