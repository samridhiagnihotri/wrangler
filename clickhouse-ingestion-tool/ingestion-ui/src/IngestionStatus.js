// src/IngestionStatus.js
import React from 'react';

function IngestionStatus({ status }) {
  return (
    <div className="p-4 mt-4 rounded-xl shadow-md bg-white">
      <h2 className="text-lg font-semibold">Ingestion Status:</h2>
      <p className={`mt-2 ${status.includes('Success') ? 'text-green-600' : 'text-red-600'}`}>
        {status}
      </p>
    </div>
  );
}

export default IngestionStatus;
