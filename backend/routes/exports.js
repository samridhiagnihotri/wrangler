// routes/export.js
router.post('/', async (req, res) => {
    const { table, columns } = req.body;
    const query = `SELECT ${columns.join(',')} FROM ${table}`;
    const stream = clickhouse.query(query, { format: 'CSVWithNames' }).stream();
    res.setHeader('Content-Disposition', `attachment; filename="${table}.csv"`);
    stream.pipe(res);
  });
  