# 가져올 노드 버전
FROM node:18.18.2 AS dist

# 경로 설정
WORKDIR /app

# 워킹 디렉토리에 package.json 복사
COPY package*.json ./

# npm 의존성 설치
RUN npm install --global pnpm
RUN pnpm install

# 모든 디렉토리 복사
COPY . ./

# 프로젝트 빌드
RUN pnpm build

# 프로젝트 시작
CMD ["pnpm", "dev"]

FROM nginx:stable-alpine

COPY --from=dist /app/dist /usr/share/nginx/html
COPY front.conf /etc/nginx/conf.d/default.conf
EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]
