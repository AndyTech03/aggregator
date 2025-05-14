import { newsArray } from "./faker/news"

export default async function fetchNews(itemId) {
	return newsArray.find(i => i.id == itemId)
}