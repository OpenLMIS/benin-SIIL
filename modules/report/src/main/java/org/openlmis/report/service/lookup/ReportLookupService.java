/*
 * This program was produced for the U.S. Agency for International Development. It was prepared by the USAID | DELIVER PROJECT, Task Order 4. It is part of a project which utilizes code originally licensed under the terms of the Mozilla Public License (MPL) v2 and therefore is licensed under MPL v2 or later.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the Mozilla Public License as published by the Mozilla Foundation, either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Mozilla Public License for more details.
 *
 * You should have received a copy of the Mozilla Public License along with this program. If not, see http://www.mozilla.org/MPL/
 */

package org.openlmis.report.service.lookup;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.*;
import org.openlmis.core.domain.GeographicLevel;
import org.openlmis.core.repository.mapper.FacilityApprovedProductMapper;
import org.openlmis.core.repository.mapper.ProcessingScheduleMapper;
import org.openlmis.core.repository.mapper.ProgramProductMapper;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.report.mapper.ReportRequisitionMapper;
import org.openlmis.report.mapper.lookup.*;
import org.openlmis.report.model.dto.*;
import org.openlmis.report.model.dto.DosageUnit;
import org.openlmis.report.model.dto.Facility;
import org.openlmis.report.model.dto.FacilityType;
import org.openlmis.report.model.dto.GeographicZone;
import org.openlmis.report.model.dto.ProcessingPeriod;
import org.openlmis.report.model.dto.Product;
import org.openlmis.report.model.dto.ProductCategory;
import org.openlmis.report.model.dto.Program;
import org.openlmis.report.model.dto.Regimen;
import org.openlmis.report.model.dto.RegimenCategory;
import org.openlmis.report.model.dto.RequisitionGroup;
import org.openlmis.report.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@NoArgsConstructor
public class ReportLookupService {

  @Autowired
  private UserSummaryExReportMapper userSummaryExReportMapper;

  @Autowired
  private RegimenReportMapper regimenReportMapper;

  @Autowired
  private ProductReportMapper productMapper;

  @Autowired
  private RequisitionGroupReportMapper rgMapper;

  @Autowired
  private ProductCategoryReportMapper productCategoryMapper;

  @Autowired
  private AdjustmentTypeReportMapper adjustmentTypeReportMapper;

  @Autowired
  private ConfigurationSettingService configurationService;

  @Autowired
  private ScheduleReportMapper scheduleMapper;

  @Autowired
  private ProgramReportMapper programMapper;

  @Autowired
  private FacilityTypeReportMapper facilityTypeMapper;

  @Autowired
  private GeographicZoneReportMapper geographicZoneMapper;

  @Autowired
  private GeographicLevelReportMapper geographicLevelMapper;

  @Autowired
  private DosageUnitReportMapper dosageUnitMapper;

  @Autowired
  private FacilityLookupReportMapper facilityReportMapper;

  @Autowired
  private ProcessingPeriodReportMapper processingPeriodMapper;

  @Autowired
  private ProcessingScheduleMapper processingScheduleMapper;

  @Autowired
  private ProductGroupReportMapper productGroupReportMapper;

  @Autowired
  private ProductFormReportMapper productFormReportMapper;

  @Autowired
  private RegimenCategoryReportMapper regimenCategoryReportMapper;

  @Autowired
  private ReportRequisitionMapper requisitionMapper;

  @Autowired
  private SupervisoryNodeReportMapper supervisoryNodeReportMapper;

  @Autowired
  private ProgramProductMapper programProductMapper;

  @Autowired
  private FacilityApprovedProductMapper facilityApprovedProductMapper;

  @Autowired
  private EquipmentTypeReportMapper equipmentTypeReportMapper;

  public List<Product> getAllProducts() {
    return productMapper.getAll();
  }

  public List<RegimenCategory> getAllRegimenCategory() {
    return regimenCategoryReportMapper.getAll();
  }

  //For created for future
  public List<RegimenCategory> getRegimenCategoryById(Long id) {
    return regimenCategoryReportMapper.getById(id);
  }

  public List<Regimen> getRegimenByProgram() {
    return regimenReportMapper.getByProgram();
  }

  public List<Regimen> getAllRegimens() {
    return regimenReportMapper.getAll();
  }

  public List<GeographicZone> getGeographicLevelById(Long geographicLevelId) {
    return geographicZoneMapper.getGeographicZoneByLevel(geographicLevelId);
  }

  public List<FlatGeographicZone> getFlatGeographicZoneList() {
    return geographicZoneMapper.getFlatGeographicZoneList();
  }

  public List<Regimen> getRegimenByCategory(Long regimenCategoryId) {
    return regimenReportMapper.getRegimenByCategory(regimenCategoryId);
  }

  public List<Product> getProductsActiveUnderProgram(Long programId) {
    if (configurationService.getBoolValue("ALLOW_PRODUCT_CATEGORY_PER_PROGRAM")) {
      return productMapper.getProductsForProgramPickCategoryFromProgramProduct(programId);
    }
    return productMapper.getProductsForProgram(programId);
  }

  public List<Product> getProductListByCategory(Integer programId, Integer categoryId) {
    if (categoryId == null || categoryId <= 0) {
      return productMapper.getAll();
    }
    return productMapper.getProductListByCategory(programId, categoryId);
  }

  public List<org.openlmis.core.domain.Product> getFullProductList() {
    return productMapper.getFullProductList();
  }

  public Product getProductByCode(String code) {
    return productMapper.getProductByCode(code);
  }

  public List<FacilityType> getFacilityTypes() {
    return facilityTypeMapper.getAll();
  }

    public List<FacilityType> getAllFacilityTypes(){
        return facilityTypeMapper.getAllFacilityTypes();
    }

  public List<FacilityType> getFacilityTypesForProgram(Long programId) {
    return facilityTypeMapper.getForProgram(programId);
  }


  public List<RequisitionGroup> getAllRequisitionGroups() {
    return this.rgMapper.getAll();
  }

  public List<RequisitionGroup> getRequisitionGroupsByProgramAndSchedule(int program, int schedule) {
    return this.rgMapper.getByProgramAndSchedule(program, schedule);
  }

  public List<RequisitionGroup> getRequisitionGroupsByProgram(int program) {
    return this.rgMapper.getByProgram(program);
  }

  public List<ProductCategory> getAllProductCategories() {
    return this.productCategoryMapper.getAll();
  }

  public List<ProductCategory> getCategoriesForProgram(int programId) {
    if (configurationService.getBoolValue("ALLOW_PRODUCT_CATEGORY_PER_PROGRAM")) {
      return this.productCategoryMapper.getForProgramUsingProgramProductCategory(programId);
    }
    return this.productCategoryMapper.getForProgram(programId);
  }

  public List<AdjustmentType> getAllAdjustmentTypes() {
    return adjustmentTypeReportMapper.getAll();
  }

  public List<Integer> getOperationYears() {


    int startYear = configurationService.getConfigurationIntValue(Constants.START_YEAR);

    Calendar calendar = Calendar.getInstance();

    int now = calendar.get(Calendar.YEAR);

    List<Integer> years = new ArrayList<>();

    if (startYear == 0 || startYear > now) {
      years.add(now);
      return years;
    }

    for (int year = startYear; year <= now; year++) {

      years.add(year);
    }

    return years;

  }

  public List<Object> getAllMonths() {

    return configurationService.getConfigurationListValue(Constants.MONTHS, ",");
  }

  public List<Program> getAllPrograms() {
    return programMapper.getAll();
  }

  public List<Program> getAllPrograms(Long userId) {
    return programMapper.getAllForUser(userId);
  }

  public Program getProgramByCode(String code) {
    return programMapper.getProgramByCode(code);
  }

  //It return all programs only with regimen
  public List<Program> getAllRegimenPrograms() {
    return programMapper.getAllRegimenPrograms();
  }
public List<Program>getAllProgramsWithBudgeting(){
    return programMapper.getAllProgramsWithBudgeting();
}

  public List<Schedule> getAllSchedules() {
    return scheduleMapper.getAll();
  }

  //TODO: implement this method
  public List<org.openlmis.report.model.dto.GeographicZone> getAllZones() {
    return geographicZoneMapper.getAll();
  }

  public List<GeographicLevel> getAllGeographicLevels() {
    return geographicLevelMapper.getAll();
  }

  public List<DosageUnit> getDosageUnits() {
    return dosageUnitMapper.getAll();
  }

  public List<Facility> getAllFacilities() {
    return facilityReportMapper.getAll();
  }

  public Facility getFacilityByCode(String code) {
    return facilityReportMapper.getFacilityByCode(code);
  }

  public List<Facility> getFacilities(Long program, Long schedule, Long type, Long requisitionGroup, Long zone, Long userId) {
    // this method does not work if no program is specified
    if (program == 0) {
      return null;
    }

    if (schedule == 0 && type == 0) {
      return facilityReportMapper.getFacilitiesByProgram(program, zone, userId);
    }

    if (type == 0 && requisitionGroup == 0) {
      return facilityReportMapper.getFacilitiesByProgramSchedule(program, schedule, zone, userId);
    }

    if (type == 0 && requisitionGroup != 0) {
      return facilityReportMapper.getFacilitiesByProgramScheduleAndRG(program, schedule, requisitionGroup, zone, userId);
    }

    if(requisitionGroup == 0){
      facilityReportMapper.getFacilitiesByPrgraomScheduleType(program, schedule, type, zone, userId);
    }

    return facilityReportMapper.getFacilitiesByPrgraomScheduleTypeAndRG(program, schedule, type, requisitionGroup, zone);
  }

  public List<Facility> getFacilitiesBy(Long userId, Long supervisoryNodeId, String requisitionGroup, Long program, Long schedule) {

      return facilityReportMapper.getFacilitiesBy(userId,supervisoryNodeId, requisitionGroup, program, schedule);

  }

  public List<Facility> getFacilityByGeographicZoneTree(Long userId, Long zoneId){
      return facilityReportMapper.getFacilitiesByGeographicZoneTree(userId,zoneId);
  }

  public List<HashMap> getFacilitiesForNotifications(Long userId, Long zoneId) {

      return facilityReportMapper.getFacilitiesForNotifications(userId, zoneId);

  }

  public List<ProcessingPeriod> getAllProcessingPeriods() {
    return processingPeriodMapper.getAll();
  }

    public List<ProcessingSchedule> getAllProcessingSchedules() {
        return processingScheduleMapper.getAll();
    }


  public List<ProcessingPeriod> getFilteredPeriods(Date startDate, Date endDate) {
    if (startDate == null && endDate == null) {
      return processingPeriodMapper.getAll();
    }
    return processingPeriodMapper.getFilteredPeriods(startDate, endDate);
  }

  public List<ProductGroup> getAllProductGroups() {
    return productGroupReportMapper.getAll();
  }

  public List<ProductForm> getAllProductForm() {
    return productFormReportMapper.getAll();
  }

  public List<Product> getListOfProducts(String productIds) {
    return productMapper.getSelectedProducts(productIds);
  }

  public String getFacilityNameForRnrId(Long rnrId) {
    return requisitionMapper.getFacilityNameForRnrId(rnrId);
  }

  public org.openlmis.core.domain.Facility getFacilityForRnrId(Long rnrId) {
    return requisitionMapper.getFacilityForRnrId(rnrId);
  }

  public String getPeriodTextForRnrId(Long rnrId) {
    return requisitionMapper.getPeriodTextForRnrId(rnrId);
  }

  public String getProgramNameForRnrId(Long rnrId) {
    return requisitionMapper.getProgramNameForRnrId(rnrId);
  }

  public List<Program> getAllUserSupervisedActivePrograms(Long userId){
      return programMapper.getUserSupervisedActivePrograms(userId);
  }
  public List<Program> getUserSupervisedActiveProgramsBySupervisoryNode(Long userId, Long supervisoryNodeId){
      return programMapper.getUserSupervisedActiveProgramsBySupervisoryNode(userId, supervisoryNodeId);
  }

  public List<SupervisoryNode> getAllUserSupervisoryNode(Long userId){
      return supervisoryNodeReportMapper.getAllSupervisoryNodesInHierarchyByUser(userId);
  }

  public List<SupervisoryNode> getAllSupervisoryNodesByUserHavingActiveProgram(Long userId){
      return supervisoryNodeReportMapper.getAllSupervisoryNodesByUserHavingActiveProgram(userId);
  }

  public List<SupervisoryNode> getAllSupervisoryNodesByParentNodeId(Long supervisoryNodeId){
      return supervisoryNodeReportMapper.getAllSupervisoryNodesByParentNodeId(supervisoryNodeId);
  }

    public List<ProgramProduct> getAllProgramProducts(){
        return programProductMapper.getAllProgramProducts();
    }

    public List<FacilityTypeApprovedProduct> getAllFacilityTypeApprovedProducts(){
        return facilityApprovedProductMapper.getAllFacilityApprovedProducts();
    }

  public List<UserRoleAssignmentsReport> getAllRolesBySupervisoryNodeHavingProgram(Long roleId,Long programId,Long supervisoryNodeId){
      return userSummaryExReportMapper.getUserRoleAssignments(roleId,programId,supervisoryNodeId);
  }

  public List<UserRoleAssignmentsReport>getUserRoleAssignments(){
      return userSummaryExReportMapper.getUserRoleAssignment();
  }


    public List<EquipmentType> getEquipmentTypes(){
        return equipmentTypeReportMapper.getEquipmentTypeList();
    }


  public GeoZoneTree getGeoZoneTree(Long userId){
    List<GeoZoneTree> zones = geographicZoneMapper.getGeoZonesForUser(userId);
    GeoZoneTree tree = geographicZoneMapper.getParentZoneTree();
    populateChildren(tree, zones);
    return tree;
  }

  public GeoZoneTree getGeoZoneTree(Long userId, Long programId){
    List<GeoZoneTree> zones = geographicZoneMapper.getGeoZonesForUserByProgram(userId, programId);
    GeoZoneTree tree = geographicZoneMapper.getParentZoneTree();
    populateChildren(tree, zones);
    return tree;
  }

  private void populateChildren(GeoZoneTree tree, List<GeoZoneTree> source){
    // find children from the source
    List<GeoZoneTree> children = new ArrayList<>();
    for(GeoZoneTree t  : source){
      if(t.getParentId() == tree.getId()) {
        children.add(t);
      }
    }

    tree.setChildren(children);

    for(GeoZoneTree zone : tree.getChildren()){
      populateChildren(zone, source);
    }
  }

  public GeoZoneTree getUserGeoZoneTree(Long userId, Long programId){
      GeoZoneTree tree = geographicZoneMapper.getParentZoneTree();
      populateUserGeographicZoneChildren(tree, userId, programId);
      return tree;
  }


  private void populateUserGeographicZoneChildren(GeoZoneTree tree, Long userId, Long programId){
      tree.setChildren(geographicZoneMapper.getUserGeographicZoneChildren(programId,tree.getId(), userId));

      for (GeoZoneTree zone : tree.getChildren()){
          populateUserGeographicZoneChildren(zone, userId, programId);
      }
  }

}
