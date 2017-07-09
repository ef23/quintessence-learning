var express = require('express');
var router = express.Router();
var firebase = require('firebase');

router.get('/', function(req, res, next) {
	res.render('login');
});

router.post('/', function(req, res, next){
	firebase.auth().signInWithEmailAndPassword(req.body.email, req.body.password)
	.then(function(authData){
		res.redirect('/profile');	
	}).catch(function(err){
 		console.log(err);
		res.redirect('/');
 	});
});


module.exports = router;
