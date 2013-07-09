package org.openlmis.core.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.RegimenColumn;
import org.openlmis.core.domain.RegimenTemplate;
import org.openlmis.core.repository.RegimenColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class RegimenColumnService {

  @Autowired
  RegimenColumnRepository repository;

  @Autowired
  MessageService messageService;

  @Autowired
  ProgramService programService;

  public void save(RegimenTemplate regimenTemplate, Long userId) {
    repository.save(regimenTemplate, userId);
    programService.setRegimenTemplateConfigured(regimenTemplate.getProgramId());
  }

  public List<RegimenColumn> getRegimenColumnsByProgramId(Long programId) {
    return repository.getRegimenColumnsByProgramId(programId);
  }

  public RegimenTemplate getRegimenTemplate(Long programId) {
    List<RegimenColumn> regimenColumns = repository.getRegimenColumnsByProgramId(programId);
    if (regimenColumns == null || regimenColumns.size() == 0) {
      regimenColumns = repository.getMasterRegimenColumnsByProgramId();
    }
    return new RegimenTemplate(programId, regimenColumns);
  }
}
