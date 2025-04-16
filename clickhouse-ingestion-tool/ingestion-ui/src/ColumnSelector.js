// src/ColumnSelector.js
import React from 'react';

function ColumnSelector({ columns, selected, onChange }) {
  return (
    <div className="p-4 mt-4 border rounded-xl shadow-md bg-white">
      <h2 className="text-lg font-semibold mb-2">Select Columns to Ingest:</h2>
      <div className="grid grid-cols-2 gap-2">
        {columns.map((col) => (
          <label key={col} className="flex items-center">
            <input
              type="checkbox"
              value={col}
              checked={selected.includes(col)}
              onChange={(e) => {
                if (e.target.checked) {
                  onChange([...selected, col]);
                } else {
                  onChange(selected.filter((item) => item !== col));
                }
              }}
            />
            <span className="ml-2">{col}</span>
          </label>
        ))}
      </div>
    </div>
  );
}

export default ColumnSelector;
