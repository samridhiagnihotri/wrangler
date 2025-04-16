// routes/upload.js
const express = require('express');
const router = express.Router();
const upload = require('../middleware/upload');
router.post('/', upload.single('file'), (req, res) => {
  
});
module.exports = router;
