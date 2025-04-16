// utils/clickhouse.js
const { ClickHouse } = require('@apla/clickhouse');
const clickhouse = new ClickHouse({
  host: 'localhost',
  port: 8123,
  user: 'default',
  password: '',
  database: 'default',
});
module.exports = clickhouse;
