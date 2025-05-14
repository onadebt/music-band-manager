from locust import HttpUser, task, between, tag, events
import json
import random
import datetime
import time
import requests
import uuid
from config import USER_SERVICE, BAND_SERVICE, MUSIC_SERVICE, TOUR_SERVICE, AUTH_TOKEN, DEFAULT_TIMEOUT

BAND_NAMES = ["Sonic Wave", "Electric Pulse", "Midnight Serenade", "Rhythm Rebels", "Harmony Junction"]
MUSICIAN_NAMES = ["John Smith", "Emma Johnson", "Michael Lee", "Sarah Wilson", "David Brown", "Lisa Chen"]
MUSICAL_STYLES = ["Rock", "Jazz", "Pop", "Hip-Hop", "Classical", "Electronic", "Metal", "Folk", "Indie", "Punk"]
CITIES = ["New York", "Los Angeles", "Chicago", "Austin", "Seattle", "Nashville", "Boston", "Miami"]
SONG_NAMES = ["Summer Dreams", "Midnight Symphony", "Electric Soul", "Mountain Echo", "City Lights", "Ocean Breeze"]
ALBUM_NAMES = ["Eternal Echoes", "Neon Nights", "Harmonic Journey", "Sonic Revolution", "Melodic Horizons"]
FIRST_NAMES = ["Alex", "Jordan", "Taylor", "Morgan", "Casey", "Riley", "Jamie", "Drew", "Quinn", "Avery"]
LAST_NAMES = ["Anderson", "Martinez", "Johnson", "Williams", "Davis", "Brown", "Wilson", "Thompson", "Garcia", "Taylor"]
COMPANY_NAMES = ["Harmony Productions", "SoundWave Management", "Stellar Artists Agency", "Rhythm Republic", "Melodic Ventures"]
STAGE_NAMES = ["DJ Thunder", "Melody Maker", "The Wizard", "Harmony Queen", "Beat Master", "Sonic Star", "Groove Machine"]
SKILLS = ["Guitar", "Bass", "Drums", "Vocals", "Keyboard", "Saxophone", "Violin", "Trumpet", "Production", "Songwriting"]

def unique_suffix():
    return uuid.uuid4().hex[:8]

class BandManagerUser(HttpUser):
    def on_start(self):
        self.band_ids = []
        self.artist_ids = []
        self.manager_ids = []
        self.tour_ids = []
        self.album_ids = []
        self.song_ids = []
        self.offer_ids = []
        
        self.auth_token = AUTH_TOKEN
        self.auth_headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {self.auth_token}"
        }
        
        self.register_manager()
        
        for _ in range(2):
            self.register_artist()
            
        self.create_band()
    
    def make_request(self, method, url, name, **kwargs):
        req_method = getattr(requests, method.lower())
        start_time = time.time()
        
        if 'headers' not in kwargs:
            kwargs['headers'] = self.auth_headers
        
        try:
            response = req_method(url, **kwargs)
            response_time = (time.time() - start_time) * 1000
            content_length = len(response.content)
            
            self.environment.events.request.fire(
                request_type=method,
                name=name,
                response_time=response_time,
                response_length=content_length,
                exception=None if response.status_code < 400 else Exception(f"Status code: {response.status_code}"),
                response=response
            )
            
            return response
            
        except Exception as e:
            response_time = (time.time() - start_time) * 1000
            self.environment.events.request.fire(
                request_type=method,
                name=name,
                response_time=response_time,
                response_length=0,
                exception=e,
                response=None
            )
            raise e
    
    @tag('users')
    @task(1)
    def register_manager(self):
        suffix = unique_suffix()
        username = f"manager_{suffix}"
        manager_data = {
            "username": username,
            "password": "password123",
            "email": f"{username}@example.com",
            "firstName": random.choice(FIRST_NAMES),
            "lastName": random.choice(LAST_NAMES),
            "role": "MANAGER",
            "companyName": random.choice(COMPANY_NAMES),
            "managedBandIds": []
        }
        
        response = self.make_request(
            'post',
            f"{USER_SERVICE}/api/managers",
            "/api/managers [POST]",
            json=manager_data,
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 201:
            try:
                manager_id = response.json().get("id")
                if manager_id:
                    self.manager_ids.append(manager_id)
                    self.manager_id = manager_id
            except (json.JSONDecodeError, AttributeError):
                pass
    
    @tag('users')
    @task(1)
    def register_artist(self):
        suffix = unique_suffix()
        username = f"manager_{suffix}"
        artist_data = {
            "username": username,
            "password": "password123",
            "email": f"{username}@example.com",
            "firstName": random.choice(FIRST_NAMES),
            "lastName": random.choice(LAST_NAMES),
            "role": "ARTIST",
            "stageName": random.choice(STAGE_NAMES) + str(random.randint(1, 100)),
            "bio": f"Passionate musician with {random.randint(1, 20)} years of experience",
            "skills": random.choice(SKILLS) + ", " + random.choice(SKILLS),
            "bandIds": []
        }
        
        response = self.make_request(
            'post',
            f"{USER_SERVICE}/api/artists",
            "/api/artists [POST]",
            json=artist_data,
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            try:
                artist_id = response.json().get("id")
                if artist_id:
                    self.artist_ids.append(artist_id)
            except (json.JSONDecodeError, AttributeError):
                pass
    
    @tag('users')
    @task(1)
    def get_all_artists(self):
        response = self.make_request(
            'get',
            f"{USER_SERVICE}/api/artists",
            "/api/artists [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            if not self.artist_ids and response.json():
                try:
                    for artist in response.json()[:3]:
                        if artist.get('id'):
                            self.artist_ids.append(artist['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('users')
    @task(1)
    def get_all_managers(self):
        response = self.make_request(
            'get',
            f"{USER_SERVICE}/api/managers",
            "/api/managers [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            if not self.manager_ids and response.json():
                try:
                    for manager in response.json()[:3]:
                        if manager.get('id'):
                            self.manager_ids.append(manager['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('users')
    @task(1)
    def get_artist_by_id(self):
        if not self.artist_ids:
            return
            
        artist_id = random.choice(self.artist_ids)
        self.make_request(
            'get',
            f"{USER_SERVICE}/api/artists/{artist_id}",
            "/api/artists/{id} [GET]",
            headers=self.auth_headers,
            timeout=5
        )
    
    @tag('users')
    @task(1)
    def get_manager_by_id(self):
        if not self.manager_ids:
            return
            
        manager_id = random.choice(self.manager_ids)
        self.make_request(
            'get',
            f"{USER_SERVICE}/api/managers/{manager_id}",
            "/api/managers/{id} [GET]",
            headers=self.auth_headers,
            timeout=5
        )
    
    @tag('users')
    @task(1)
    def update_artist(self):
        if not self.artist_ids:
            return
            
        artist_id = random.choice(self.artist_ids)
        suffix = unique_suffix()
        update_data = {
            "username": f"artist_{suffix}",
            "email": f"updated_artist_{suffix}@example.com",
            "firstName": random.choice(FIRST_NAMES),
            "lastName": random.choice(LAST_NAMES),
            "stageName": random.choice(STAGE_NAMES) + str(random.randint(1, 100)),
            "bio": f"Updated bio with {random.randint(1, 20)} years of experience",
            "skills": random.choice(SKILLS) + ", " + random.choice(SKILLS)
        }
        
        self.make_request(
            'put',
            f"{USER_SERVICE}/api/artists/{artist_id}",
            "/api/artists/{id} [PUT]",
            json=update_data,
            headers=self.auth_headers,
            timeout=5
        )
    
    @tag('users')
    @task(1)
    def update_manager(self):
        if not self.manager_ids:
            return
            
        manager_id = random.choice(self.manager_ids)
        suffix = unique_suffix()
        update_data = {
            "username": f"manager_{suffix}",
            "email": f"updated_manager_{suffix}@example.com",
            "firstName": random.choice(FIRST_NAMES),
            "lastName": random.choice(LAST_NAMES),
            "companyName": random.choice(COMPANY_NAMES) + f" {random.randint(1, 100)}"
        }
        
        self.make_request(
            'put',
            f"{USER_SERVICE}/api/managers/{manager_id}",
            "/api/managers/{id} [PUT]",
            json=update_data,
            headers=self.auth_headers,
            timeout=5
        )
    
    @tag('users')
    @task(1)
    def update_artist_bands(self):
        if not self.artist_ids or not self.band_ids:
            return
            
        artist_id = random.choice(self.artist_ids)
        num_bands = min(len(self.band_ids), random.randint(1, 3))
        selected_band_ids = random.sample(self.band_ids, num_bands)
        
        self.make_request(
        'patch',
        f"{USER_SERVICE}/api/artists/bands/{artist_id}",
        "/api/artists/bands/{id} [PATCH]",
        json=selected_band_ids,
        headers=self.auth_headers,
        timeout=5
        )
    
    @tag('users')
    @task(1)
    def update_manager_bands(self):
        if not self.manager_ids or not self.band_ids:
            return
            
        manager_id = random.choice(self.manager_ids)
        num_bands = min(len(self.band_ids), random.randint(1, 3))
        selected_band_ids = random.sample(self.band_ids, num_bands)
        
        self.make_request(
        'patch',
        f"{USER_SERVICE}/api/managers/bands/{manager_id}",
        "/api/managers/bands/{id} [PATCH]",
        json=selected_band_ids,
        headers=self.auth_headers,
        timeout=5
        )
    
    @tag('users')
    @task(1)
    def link_artist_to_band(self):
        if not self.artist_ids or not self.band_ids:
            return
            
        artist_id = random.choice(self.artist_ids)
        band_id = random.choice(self.band_ids)
        
        self.make_request(
        'patch',
        f"{USER_SERVICE}/api/artists/link/{artist_id}/{band_id}", 
        "/api/artists/link/{artistId}/{bandId} [PATCH]",
        headers=self.auth_headers,
        timeout=5
)

    @tag('users')
    @task(1)
    def unlink_artist_from_band(self):
        if not self.artist_ids or not self.band_ids:
            return
            
        artist_id = random.choice(self.artist_ids)
        band_id = random.choice(self.band_ids)
        
        self.make_request(
        'patch',
        f"{USER_SERVICE}/api/artists/unlink/{artist_id}/{band_id}",
        "/api/artists/unlink/{artistId}/{bandId} [PATCH]",
        headers=self.auth_headers,
        timeout=5
)
    
    @tag('bands')
    @task(2)
    def get_all_bands(self):
        response = self.make_request(
            'get',
            f"{BAND_SERVICE}/api/bands",
            "/api/bands [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            if not self.band_ids and response.json():
                try:
                    for band in response.json()[:3]:
                        if band.get('id'):
                            self.band_ids.append(band['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('bands')
    @task(1)
    def create_band(self):
        if not self.manager_ids:
            return
            
        band_name = random.choice(BAND_NAMES) + " " + str(random.randint(1, 100))
        musical_style = random.choice(MUSICAL_STYLES)
        manager_id = random.choice(self.manager_ids)
        
        response = self.make_request(
            'post',
            f"{BAND_SERVICE}/api/bands?name={band_name}&musicalStyle={musical_style}&managerId={manager_id}",
            "/api/bands [POST]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 201:
            try:
                band_id = response.json().get("id")
                if band_id:
                    self.band_ids.append(band_id)
            except (json.JSONDecodeError, AttributeError):
                pass
    
    @tag('bands')
    @task(2)
    def get_band_by_id(self):
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        self.make_request(
            'get',
            f"{BAND_SERVICE}/api/bands/{band_id}",
            "/api/bands/{id} [GET]",
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
    
    @tag('bands')
    @task(1)
    def update_band(self):
        if not self.band_ids or not self.manager_ids:
            return
            
        band_id = random.choice(self.band_ids)
        manager_id = random.choice(self.manager_ids)
        update_data = {
            "id": band_id,
            "name": random.choice(BAND_NAMES) + " " + str(random.randint(1, 100)),
            "musicalStyle": random.choice(MUSICAL_STYLES),
            "managerId": manager_id,
            "logo": f"https://example.com/logos/band{random.randint(1,50)}.png"
        }
        
        self.make_request(
            'patch',
            f"{BAND_SERVICE}/api/bands",
            "/api/bands [PATCH]",
            json=update_data,
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
    
    @tag('bands')
    @task(1)
    def create_band_offer(self):
        if not self.band_ids or not self.artist_ids or not self.manager_ids:
            return
            
        if hasattr(self, 'manager_id') and self.manager_id:
            manager_id = self.manager_id
        else:
            manager_id = random.choice(self.manager_ids)
            
        band_id = random.choice(self.band_ids)
        
        potential_artists = [artist_id for artist_id in self.artist_ids]
        
        if not potential_artists:
            return
            
        artist_id = random.choice(potential_artists)
        
        response = self.make_request(
            'post',
            f"{BAND_SERVICE}/api/bands/offers?bandId={band_id}&invitedMusicianId={artist_id}&offeringManagerId={manager_id}",
            "/api/bands/offers [POST]",
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
    
    @tag('bands')
    @task(1)
    def respond_to_band_offer(self):
        if not self.offer_ids:
            return
            
        offer_id = random.choice(self.offer_ids)
        action = "accept" if random.random() > 0.5 else "reject"
        
        response = self.make_request(
            'post',
            f"{BAND_SERVICE}/api/bands/offers/{offer_id}/{action}",
            f"/api/bands/offers/{{id}}/{action} [POST]",
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
        
        if response.status_code in [200, 201]:
            self.offer_ids.remove(offer_id)
    
    @tag('bands')
    @task(1)
    def add_band_member(self):
        if not self.band_ids or not self.artist_ids:
            return
            
        band_id = random.choice(self.band_ids)
        artist_id = random.choice(self.artist_ids)
        
        response = self.make_request(
                'patch',
                f"{BAND_SERVICE}/api/bands/{band_id}/members/{artist_id}",
                "/api/bands/{id}/members/{id} [PATCH]",
                headers=self.auth_headers,
                timeout=DEFAULT_TIMEOUT
            )
        if response.status_code == 400:
            print(f"[WARN] Artist {artist_id} might already be in band {band_id}")
                
    @tag('bands')
    @task(1)
    def remove_band_member(self):
        if not self.band_ids or not self.artist_ids:
            return
            
        band_id = random.choice(self.band_ids)
        artist_id = random.choice(self.artist_ids)
        
        response = self.make_request(
                'delete',
                f"{BAND_SERVICE}/api/bands/{band_id}/members/{artist_id}",
                "/api/bands/{id}/members/{id} [DELETE]",
                headers=self.auth_headers,
                timeout=DEFAULT_TIMEOUT
        )
        if response.status_code == 400:
            print(f"[WARN] Artist {artist_id} may not be in band {band_id}")
    
    @tag('tours')
    @task(1)
    def create_tour(self):
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        num_cities = random.randint(3, 6)
        city_visits = []
        
        current_date = datetime.datetime.now()
        start_date = current_date + datetime.timedelta(days=random.randint(30, 60))
        
        for i in range(num_cities):
            date_from = start_date + datetime.timedelta(days=i*3)
            date_to = date_from + datetime.timedelta(days=random.randint(1, 3))
            
            city_visits.append({
                "cityName": random.choice(CITIES),
                "dateFrom": date_from.strftime("%Y-%m-%d"),
                "dateTo": date_to.strftime("%Y-%m-%d")
            })
        
        tour_data = {
            "bandId": band_id,
            "tourName": f"Summer Festival Tour {random.randint(1, 100)}",
            "cityVisits": city_visits
        }
        
        response = self.make_request(
            'post',
            f"{TOUR_SERVICE}/api/tours",
            "/api/tours [POST]",
            json=tour_data,
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
        
        if response.status_code == 201:
            try:
                tour_id = response.json().get("id")
                if tour_id:
                    self.tour_ids.append(tour_id)
            except (json.JSONDecodeError, AttributeError):
                pass
    
    @tag('tours')
    @task(2)
    def get_tours_by_band(self):
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        response = self.make_request(
            'get',
            f"{TOUR_SERVICE}/api/tours/band/{band_id}",
            "/api/tours/band/{id} [GET]",
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
        
        if response.status_code == 200:
            if not self.tour_ids and response.json():
                try:
                    for tour in response.json()[:3]:
                        if tour.get('id'):
                            self.tour_ids.append(tour['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('tours')
    @task(1)
    def update_tour(self):
        if not self.tour_ids:
            return
            
        tour_id = random.choice(self.tour_ids)
        
        response = self.make_request(
            'get',
            f"{TOUR_SERVICE}/api/tours/{tour_id}",
            "/api/tours/{id} [GET for update]",
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
        
        if response.status_code != 200:
            return
                
        try:
            tour_data = response.json()
            tour_data["tourName"] = f"Updated Festival Tour {random.randint(1, 100)}"
            
            self.make_request(
                'put',
                f"{TOUR_SERVICE}/api/tours/{tour_id}",
                "/api/tours/{id} [PUT]",
                json=tour_data,
                headers=self.auth_headers,
                timeout=DEFAULT_TIMEOUT
            )
        except (json.JSONDecodeError, AttributeError, KeyError):
            pass
    
    @tag('tours')
    @task(1)
    def add_city_to_tour(self):
        if not self.tour_ids:
            return
            
        tour_id = random.choice(self.tour_ids)
        
        current_date = datetime.datetime.now()
        date_from = current_date + datetime.timedelta(days=random.randint(60, 90))
        date_to = date_from + datetime.timedelta(days=random.randint(1, 3))
        
        city_visit = {
            "cityName": random.choice(CITIES),
            "dateFrom": date_from.strftime("%Y-%m-%d"),
            "dateTo": date_to.strftime("%Y-%m-%d")
        }
        
        self.make_request(
            'post',
            f"{TOUR_SERVICE}/api/tours/{tour_id}/city-visit",
            "/api/tours/{id}/city-visit [POST]",
            json=city_visit,
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
    
    @tag('albums')
    @task(1)
    def create_album(self):
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        current_date = datetime.datetime.now()
        release_date = current_date - datetime.timedelta(days=random.randint(30, 365))
        
        album_data = {
            "title": random.choice(ALBUM_NAMES) + " " + str(random.randint(1, 100)),
            "releaseDate": release_date.strftime("%Y-%m-%dT%H:%M:%S"),
            "bandId": band_id,
            "songs": [] 
        }
        
        response = self.make_request(
            'post',
            f"{MUSIC_SERVICE}/api/albums",
            "/api/albums [POST]",
            json=album_data,
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
        
        if response.status_code == 201:
            try:
                album_id = response.json().get("id")
                if album_id:
                    self.album_ids.append(album_id)
            except (json.JSONDecodeError, AttributeError):
                pass
    
    @tag('songs')
    @task(1)
    def add_song(self):
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        album_id = random.choice(self.album_ids) if self.album_ids and random.random() > 0.5 else None
        
        song_data = {
            "name": random.choice(SONG_NAMES) + " " + str(random.randint(1, 100)),
            "duration": random.randint(180, 360),
            "bandId": band_id
        }
        
        if album_id:
            song_data["albumId"] = album_id
        
        response = self.make_request(
            'post',
            f"{MUSIC_SERVICE}/api/songs",
            "/api/songs [POST]",
            json=song_data,
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
        
        if response.status_code == 201:
            try:
                song_id = response.json().get("id")
                if song_id:
                    self.song_ids.append(song_id)
            except (json.JSONDecodeError, AttributeError):
                pass
    
    @tag('songs')
    @task(2)
    def get_songs_by_band(self):
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        response = self.make_request(
            'get',
            f"{MUSIC_SERVICE}/api/songs/band/{band_id}",
            "/api/songs/band/{id} [GET]",
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
        
        if response.status_code == 200:
            if not self.song_ids and response.json():
                try:
                    for song in response.json()[:5]:
                        if song.get('id'):
                            self.song_ids.append(song['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('albums')
    @task(2)
    def get_albums_by_band(self):
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        response = self.make_request(
            'get',
            f"{MUSIC_SERVICE}/api/albums/band/{band_id}",
            "/api/albums/band/{id} [GET]",
            headers=self.auth_headers,
            timeout=DEFAULT_TIMEOUT
        )
        
        if response.status_code == 200:
            if not self.album_ids and response.json():
                try:
                    for album in response.json()[:3]:
                        if album.get('id'):
                            self.album_ids.append(album['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass