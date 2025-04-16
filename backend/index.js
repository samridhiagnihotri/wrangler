
// index.js
const express = require('express');
const app = express();
app.use(express.json());
app.use(cors());
app.listen(5000, () => console.log('Server running on port 5000'));
