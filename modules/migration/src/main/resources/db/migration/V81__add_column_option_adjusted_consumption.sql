UPDATE master_rnr_columns
set calculationOption = '[{"name":"Default", "id":"DEFAULT"},{"name":"Dispensed Quantity + No of New Patients","id":"DISPENSED_PLUS_NEW_PATIENTS"},{"name":"(Max x 90) / (90 - Stockout Days)","id":"MAX_X_90"}]'
where name = 'normalizedConsumption';