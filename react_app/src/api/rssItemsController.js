import { apiHost } from "../routes";

const host = `${apiHost}/rss_items`
export async function fetchLatest(offset, limit) {
	return fetch(`${host}/latest`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			offset, 
			limit,
		})
	}).then((response) => response.json())
}

export async function getItem(newsId) {
	return fetch(`${host}/getItem`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			id: newsId, 
		})
	}).then((response) => response.json())
}