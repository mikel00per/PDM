// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.sendNewPeticionNotification = functions.database.ref('/peticiones/{idUser}/nuevas/{idPeticion}')
    .onWrite(async (change, context) => {
        const idUser = context.params.idUser;
        const idPeticion = context.params.idPeticion;
        // If un-follow we exit the function.
        if (!change.after.val()) {
            return console.log('User ', idUser, 'pierde peteticon: ', idPeticion);
        }
        console.log('NuevaPeticion:', idPeticion, 'to user:', idUser);

        var userName, userImg, userToken;
        // Get the list of device notification tokens.
        const getDeviceTokensPromise = admin.database()
            .ref(`/users/${idUser}/tokenMobile`).once('value');

        // Get the follower profile.
        const getUserName = admin.database()
            .ref(`/users/${idUser}/nombre`).once('value');

        const getUserIdFrom = admin.database()
            .ref(`/peticiones/${idUser}/nuevas/${idPeticion}/from`).once('value');

        // The snapshot to the user's tokens.
        let tokensSnapshot;


        const results = await Promise.all([getDeviceTokensPromise, getUserName, getUserIdFrom]);
        userTokenSnapshot = results[0];
        userNameSnapshot = results[1];
        userIdFromSnapshot = results[2];

        const getUserNameFrom = admin.database()
            .ref(`/users/${userIdFromSnapshot.val()}/nombre`).once('value');

        const getUserImgFrom = admin.database()
            .ref(`/users/${userIdFromSnapshot.val()}/imagenPerfil`).once('value');

        const results2 = await Promise.all([getUserNameFrom, getUserImgFrom]);
        userNameFromSnapshot = results2[0];
        userImgFromSnapshot = results2[1];

        // Check if there are any device tokens.
        if (userTokenSnapshot.val() == null) {
            return console.log('There are no notification tokens to send to.');
        }

        console.log('There are a token ', userTokenSnapshot.val(), ' to send notifications to.');
        console.log('Fetched follower id', userIdFromSnapshot.val());
        console.log('Fetched follower profile', userNameFromSnapshot.val());
        console.log('id_up', userIdFromSnapshot.val());
        console.log('id_p', idPeticion);

        console.log('Img', userImgFromSnapshot.val());

        // Notification detailsx.
        const payload = {
            data: {
                title: '¡Peticion de contacto!',
                body: `¡@${userNameFromSnapshot.val()} necesita tu contacto!`,
                picture_url: `${userImgFromSnapshot.val()}`,
                id_up: `${userIdFromSnapshot.val()}`,
                id_p: `${idPeticion}`
            }
        };

        // Listing all tokens as an array.
            tokens = Object.keys(userTokenSnapshot.val());
        // Send notifications to token
        const response = await admin.messaging().sendToDevice(userTokenSnapshot.val(), payload);

    });

