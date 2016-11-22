Feature: add budget

Scenario: add budget success
  When add 1000 at "2016-01"
  Then DB has one record with "2016-01" 1000