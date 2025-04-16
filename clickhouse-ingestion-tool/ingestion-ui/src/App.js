import React, { useState } from 'react';
import FileUpload from './FileUpload';
import ColumnSelector from './ColumnSelector';
import IngestionStatus from './IngestionStatus';
import axios from 'axios';

function App() {
  const [file, setFile] = useState(null);
  const [columns, setColumns] = useState([]);
  const [selectedCols, setSelectedCols] = useState([]);
  const [status, setStatus] = useState('');

  const onFileUploaded = async (uploadedFile) => {
    setFile(uploadedFile);

    // Simulate column extraction (in real case, ask backend)
    const reader = new FileReader();
    reader.onload = () => {
      const lines = reader.result.split('\n');
      const headers = lines[0].split(',');
      setColumns(headers.map((col) => col.trim()));
    };
    reader.readAsText(uploadedFile);
  };

  const triggerIngestion = async () => {
    if (!file || selectedCols.length === 0) {
      alert('Please upload a file and select columns.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('columns', JSON.stringify(selectedCols));

    try {
      const res = await axios.post('http://localhost:5000/api/ingest', formData);
      setStatus(`✅ Success: ${res.data.message}`);
    } catch (err) {
      setStatus(`❌ Error: ${err.response?.data?.error || 'Ingestion failed'}`);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <h1 className="text-2xl font-bold mb-6">ClickHouse Ingestion Tool</h1>
      <FileUpload onFileUploaded={onFileUploaded} />
      {columns.length > 0 && (
        <ColumnSelector columns={columns} selected={selectedCols} onChange={setSelectedCols} />
      )}
      {columns.length > 0 && (
        <button
          onClick={triggerIngestion}
          className="mt-4 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          Ingest Data
        </button>
      )}
      {status && <IngestionStatus status={status} />}
    </div>
  );
}

export default App;
