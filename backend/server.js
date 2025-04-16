const express = require('express');
const cors = require('cors');
const multer = require('multer');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = 5000;

// Enable CORS
app.use(cors());

// Multer for file uploads
const upload = multer({ dest: 'uploads/' });

app.post('/api/ingest', upload.single('file'), async (req, res) => {
  try {
    console.log('Received file:', req.file);
    console.log('Received columns:', req.body.columns);

    if (!req.file) {
      return res.status(400).json({ error: 'No file uploaded' });
    }

    if (!req.body.columns) {
      return res.status(400).json({ error: 'No columns provided' });
    }

    const columns = JSON.parse(req.body.columns);
    const filePath = req.file.path;

    // Simulate ClickHouse ingestion
    console.log('Ingesting into ClickHouse...');
    // TODO: Replace with actual ClickHouse ingestion logic

    return res.status(200).json({ message: 'Ingestion completed successfully' });
  } catch (err) {
    console.error('Ingestion error:', err);
    return res.status(500).json({ error: 'Ingestion failed' });
  }
});

app.listen(PORT, () => {
  console.log(`✅ Server running at http://localhost:${PORT}`);
});
