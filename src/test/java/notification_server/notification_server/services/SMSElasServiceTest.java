package notification_server.notification_server.services;

import notification_server.notification_server.entity.SMSElas;
import notification_server.notification_server.repository.SMSElasRepositoryHandler;
import notification_server.notification_server.service.SMSElasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SMSElasServiceTest {

    @Mock
    private SMSElasRepositoryHandler smselasRepositoryHandler;

    @InjectMocks
    private SMSElasService smselasService;

    private SMSElas smselas;
    private UUID id;

    @BeforeEach
    public void setUp() {
        smselas = new SMSElas();
        id = UUID.randomUUID();
        smselas.setId(id);
        smselas.setMessage("Test message");
        smselas.setCreatedAt(new Date());
    }

    @Test
    public void testSaveSMSElas() {
        when(smselasRepositoryHandler.save(smselas)).thenReturn(smselas);

        SMSElas savedSMSElas = smselasService.save(smselas);

        assertNotNull(savedSMSElas);
        assertEquals(smselas.getId(), savedSMSElas.getId());
        assertEquals(smselas.getMessage(), savedSMSElas.getMessage());
        verify(smselasRepositoryHandler, times(1)).save(smselas);
    }

    @Test
    public void testGetAllSMSElas() {
        List<SMSElas> smselasList = new ArrayList<>();
        smselasList.add(smselas);
        when(smselasRepositoryHandler.findAll()).thenReturn(smselasList);

        List<SMSElas> result = smselasService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(smselas.getMessage(), result.get(0).getMessage());
        verify(smselasRepositoryHandler, times(1)).findAll();
    }

    @Test
    public void testDeleteAllSMSElas() {
        smselasService.deleteAll();

        verify(smselasRepositoryHandler, times(1)).deleteAll();
    }

    @Test
    public void testFindByCreatedAtBetweenWithPagination() {
        String startTimeString  = "2025-02-01T08:13:58.000+00:00";
        String endTimeString = "2025-02-11T08:13:58.000+00:00";

        OffsetDateTime startTime = OffsetDateTime.parse(startTimeString);
        OffsetDateTime endTime = OffsetDateTime.parse(endTimeString);

        Date startLocalDateTime = Date.from(startTime.toInstant());
        Date endLocalDateTime = Date.from(endTime.toInstant());

        Pageable pageable = PageRequest.of(0, 10);
        List<SMSElas> smselasList = new ArrayList<>();
        smselasList.add(smselas);
        Page<SMSElas> page = mock(Page.class);
        when(page.getContent()).thenReturn(smselasList);
        when(smselasRepositoryHandler.findByCreatedAtBetween(startLocalDateTime, endLocalDateTime, pageable)).thenReturn(page);

        List<SMSElas> result = smselasService.findByCreatedAtBetween(startTimeString, endTimeString, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(smselas.getMessage(), result.get(0).getMessage());
        verify(smselasRepositoryHandler, times(1)).findByCreatedAtBetween(startLocalDateTime, endLocalDateTime, pageable);
    }

    @Test
    public void testSearchTextWithPagination() {
        String text = "Test";
        Pageable pageable = PageRequest.of(0, 10);
        List<SMSElas> smselasList = new ArrayList<>();
        smselasList.add(smselas);
        Page<SMSElas> page = mock(Page.class);
        when(page.getContent()).thenReturn(smselasList);
        when(smselasRepositoryHandler.findByMessageContaining(text, pageable)).thenReturn(page);

        List<SMSElas> result = smselasService.searchByText(text, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(smselas.getMessage(), result.get(0).getMessage());
        verify(smselasRepositoryHandler, times(1)).findByMessageContaining(text, pageable);
    }
}
