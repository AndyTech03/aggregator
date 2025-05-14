import uuid from 'react-uuid'
import { faker } from '@faker-js/faker'


export const usersArray = []
for (let i = 0; i < 1000; i ++) {
	usersArray.push({
		id: uuid(),
		username: faker.internet.username(),
		password: faker.internet.password(),
	})
}

