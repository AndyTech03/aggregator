import { apiHost } from "../routes";

const host = `${apiHost}/social`

export async function getReaction(userId, newsId) {
	return fetch(`${host}/getReaction`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({
			userId, 
			newsId,
		})
	}).then((response) => response.json())
}

export async function setReaction(userId, newsId, type) {
	return fetch(`${host}/setReaction`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({
			userId, 
			newsId,
			type,
		})
	})
}