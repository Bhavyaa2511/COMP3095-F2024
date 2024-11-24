print('START');

db = db.getSiblingDB('product-service');

db.createUser({
    user: 'admin',
    pwd: 'password',
    roles: [{ role: 'readWrite', db: 'product-service' }]  // Removed extra space
});

db.createCollection('user');

print('END');
