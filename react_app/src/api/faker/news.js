import uuid from 'react-uuid'
import { faker } from '@faker-js/faker'
import { usersArray } from './users'

const authorsArray = []
for (let i = 0; i < 10; i++) {
	const sex = faker.person.sex()
	const firstName = faker.person.firstName({sex: sex})
	const lastName = faker.person.lastName(sex)
	authorsArray.push({
		id: uuid(),
		fullName: faker.person.fullName({firstName, lastName, sex}),
		username: faker.internet.username({firstName, lastName})
	})
}

const categoriesArray = []
for (let i = 0; i < 30; i++) {
	categoriesArray.push({
		id: uuid(),
		title: faker.word.sample(),
	})
}

const rssFeedsArray = []
for (let i = 0; i < 10; i++) {
	const title = faker.internet.domainName()
	rssFeedsArray.push({
		id: uuid(),
		title: title,
		homeUrl: `https://www.${title}/news`,
		rssUrl: `https://www.${title}/news/rss`,
	})
}

export const newsArray = []
for (let i = 0; i < 1000; i++) {
	const author = authorsArray[Math.floor(Math.random() * authorsArray.length)]
	const title = faker.word.words({count: {min: 5, max: 10}})
	const description = faker.word.words({count: {min: 10, max: 200}})
	const categories = []
	const categoriesCount = Math.floor(Math.random() * categories.length)
	const set = [...categories]
	for (let j = 0; j < categoriesCount; j++) {
		const index = Math.floor(Math.random() * set.length)
		categories.push(set[index])
	}
	const rssFeed = rssFeedsArray[Math.floor(Math.random() * rssFeedsArray.length)]
	newsArray.push({
		id: uuid(),
		author: author,
		title: title,
		description: description,
		categories: categories,
		rssFeed: rssFeed,
		date: faker.date.recent(),
		uri: `${rssFeed.homeUrl}/${title.replaceAll(' ', '-')}`
	})
}

export const viewsArray = []
export const likesArray = []
for (const user of usersArray) {
	for (const news of newsArray) {
		if (Math.random() < .3) continue;

		viewsArray.push({
			id: uuid(),
			userId: user.id,
			newsId: news.id,
			viewDate: faker.date.recent({refDate: news.date}),
			viewsCount: faker.number.int({max: 10}),
			readOrigin: Math.random() < .5 ? false : true,
		})

		if (Math.random() < .5) continue;
		likesArray.push({
			id: uuid(),
			userId: user.id,
			newsId: news.id,
			sign: Math.random() > .5 ? 'like' : 'dislike',
		})
	}
}