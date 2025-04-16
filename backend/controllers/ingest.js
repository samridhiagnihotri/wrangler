// controllers/ingest.js
const fs = require('fs');
const csv = require('fast-csv');
const clickhouse = require('../utils/clickhouse');

const ingestData = (filePath, table, columns) => {
  return new Promise((resolve, reject) => {
    const stream = fs.createReadStream(filePath);
    const data = [];
    csv.parseStream(stream, { headers: true })
      .on('data', row => data.push(row))
      .on('end', async () => {
        const values = data.map(row => `(${columns.map(col => `'${row[col]}'`).join(',')})`);
        const query = `INSERT INTO ${table} (${columns.join(',')}) VALUES ${values.join(',')}`;
        await clickhouse.query(query).toPromise();
        resolve('Ingestion complete');
      })
      .on('error', reject);
  });
};
