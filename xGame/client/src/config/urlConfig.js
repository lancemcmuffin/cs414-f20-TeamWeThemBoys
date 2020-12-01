/**
 * @module urlConfig.js
 * @description exports URL constants used by services
 */

const localhostURL = "http://localhost:8080";
const serverURL = "tbd";

const serviceURL = localhostURL;

export const matchServiceURL = `${serviceURL}/match`;
export const userServiceURL = `${serviceURL}/user`;
export const messageServiceURL = `${serviceURL}/message`;
export const gameServiceURL = `${serviceURL}/game`;
