from locust import HttpUser, task, between, tag, events
import json
import random
import datetime
import time
import requests

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

# Service endpoints
USER_SERVICE = "http://localhost:8091"
BAND_SERVICE = "http://localhost:8092"
MUSIC_SERVICE = "http://localhost:8093"
TOUR_SERVICE = "http://localhost:8094"

class BandManagerUser(HttpUser):
    """
    Simulates a band manager using the distributed Band Manager system during festival season rush
    This implementation uses direct HTTP requests to handle multiple services on different ports
    """
    
    def on_start(self):
        """Initialize user data when simulation starts"""
        self.auth_headers = {"Content-Type": "application/json"}
        
        self.band_ids = []
        self.artist_ids = []
        self.manager_ids = []
        self.tour_ids = []
        self.album_ids = []
        self.song_ids = []
        self.offer_ids = []
        
        self.register_manager()
        
        for _ in range(2):
            self.register_artist()
            
        self.create_band()
    
    def make_request(self, method, url, name, **kwargs):
        """
        Make a request to a specific service and log it in Locust statistics
        """
        req_method = getattr(requests, method.lower())
        start_time = time.time()
        
        try:
            response = req_method(url, **kwargs)
            response_time = (time.time() - start_time) * 1000
            content_length = len(response.content)
            
            # Log the request in Locust stats
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
    
    # User Service Tasks
    
    @tag('users')
    @task(1)
    def register_manager(self):
        """Register a new manager account"""
        username = f"manager_{random.randint(1000, 9999)}"
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
                    self.manager_id = manager_id  # Store the last created manager ID for use in other tasks
            except (json.JSONDecodeError, AttributeError):
                pass
    
    @tag('users')
    @task(1)
    def register_artist(self):
        """Register a new artist account"""
        username = f"artist_{random.randint(1000, 9999)}"
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
        """Get all artists"""
        response = self.make_request(
            'get',
            f"{USER_SERVICE}/api/artists",
            "/api/artists [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            # Store some artist IDs if we don't have any yet
            if not self.artist_ids and response.json():
                try:
                    for artist in response.json()[:3]:  # Get up to 3 artists
                        if artist.get('id'):
                            self.artist_ids.append(artist['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('users')
    @task(1)
    def get_all_managers(self):
        """Get all managers"""
        response = self.make_request(
            'get',
            f"{USER_SERVICE}/api/managers",
            "/api/managers [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            # Store some manager IDs if we don't have any yet
            if not self.manager_ids and response.json():
                try:
                    for manager in response.json()[:3]:  # Get up to 3 managers
                        if manager.get('id'):
                            self.manager_ids.append(manager['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('users')
    @task(1)
    def get_artist_by_id(self):
        """Get a specific artist by ID"""
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
        """Get a specific manager by ID"""
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
        """Update an artist's information"""
        if not self.artist_ids:
            return
            
        artist_id = random.choice(self.artist_ids)
        update_data = {
            "username": f"artist_{random.randint(1000, 9999)}",
            "email": f"updated_artist_{random.randint(1000, 9999)}@example.com",
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
        """Update a manager's information"""
        if not self.manager_ids:
            return
            
        manager_id = random.choice(self.manager_ids)
        update_data = {
            "username": f"manager_{random.randint(1000, 9999)}",
            "email": f"updated_manager_{random.randint(1000, 9999)}@example.com",
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
        """Update an artist's band associations"""
        if not self.artist_ids or not self.band_ids:
            return
            
        artist_id = random.choice(self.artist_ids)
        # Choose a random subset of band IDs
        num_bands = min(len(self.band_ids), random.randint(1, 3))
        selected_band_ids = random.sample(self.band_ids, num_bands)
        
        self.make_request(
            'post',
            f"{USER_SERVICE}/api/artists/bands/{artist_id}",
            "/api/artists/bands/{id} [POST]",
            json=selected_band_ids,
            headers=self.auth_headers,
            timeout=5
        )
    
    @tag('users')
    @task(1)
    def update_manager_bands(self):
        """Update a manager's band associations"""
        if not self.manager_ids or not self.band_ids:
            return
            
        manager_id = random.choice(self.manager_ids)
        # Choose a random subset of band IDs
        num_bands = min(len(self.band_ids), random.randint(1, 3))
        selected_band_ids = random.sample(self.band_ids, num_bands)
        
        self.make_request(
            'post',
            f"{USER_SERVICE}/api/managers/bands/{manager_id}",
            "/api/managers/bands/{id} [POST]",
            json=selected_band_ids,
            headers=self.auth_headers,
            timeout=5
        )
    
    # Band Management Service Tasks
    
    @tag('bands')
    @task(2)
    def get_all_bands(self):
        """View all bands - a frequent operation"""
        response = self.make_request(
            'get',
            f"{BAND_SERVICE}/api/bands",
            "/api/bands [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            # If we don't have any bands yet, store some from the response
            if not self.band_ids and response.json():
                try:
                    for band in response.json()[:3]:  # Get up to 3 bands
                        if band.get('id'):
                            self.band_ids.append(band['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('bands')
    @task(1)
    def create_band(self):
        """Create a new band"""
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
        """Get a specific band by ID"""
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        self.make_request(
            'get',
            f"{BAND_SERVICE}/api/bands/{band_id}",
            "/api/bands/{id} [GET]",
            headers=self.auth_headers,
            timeout=5
        )
    
    @tag('bands')
    @task(1)
    def update_band(self):
        """Update band information"""
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
            timeout=5
        )
    
    @tag('bands')
    @task(1)
    def create_band_offer(self):
        """Create an offer for a musician to join a band"""
        if not self.band_ids or not self.artist_ids or not self.manager_ids:
            return
            
        band_id = random.choice(self.band_ids)
        artist_id = random.choice(self.artist_ids)
        manager_id = random.choice(self.manager_ids)
        
        response = self.make_request(
            'post',
            f"{BAND_SERVICE}/api/bands/offers?bandId={band_id}&invitedMusicianId={artist_id}&offeringManagerId={manager_id}",
            "/api/bands/offers [POST]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 201:
            try:
                offer_id = response.json().get("id")
                if offer_id:
                    self.offer_ids.append(offer_id)
            except (json.JSONDecodeError, AttributeError):
                pass
    
    @tag('bands')
    @task(1)
    def get_band_offers(self):
        """Get all band offers"""
        response = self.make_request(
            'get',
            f"{BAND_SERVICE}/api/bands/offers",
            "/api/bands/offers [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            # Store some offer IDs if we don't have any yet
            if not self.offer_ids and response.json():
                try:
                    for offer in response.json()[:3]:  # Get up to 3 offers
                        if offer.get('id'):
                            self.offer_ids.append(offer['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('bands')
    @task(1)
    def respond_to_band_offer(self):
        """Accept or reject a band offer"""
        if not self.offer_ids:
            return
            
        offer_id = random.choice(self.offer_ids)
        # Randomly accept or reject the offer
        action = "accept" if random.random() > 0.5 else "reject"
        
        response = self.make_request(
            'post',
            f"{BAND_SERVICE}/api/bands/offers/{offer_id}/{action}",
            f"/api/bands/offers/{{id}}/{action} [POST]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code in [200, 201]:
            # Remove the offer ID after responding to it
            self.offer_ids.remove(offer_id)
    
    @tag('bands')
    @task(1)
    def add_band_member(self):
        """Add a member to a band"""
        if not self.band_ids or not self.artist_ids:
            return
            
        band_id = random.choice(self.band_ids)
        artist_id = random.choice(self.artist_ids)
        
        self.make_request(
            'patch',
            f"{BAND_SERVICE}/api/bands/{band_id}/members/{artist_id}",
            "/api/bands/{id}/members/{id} [PATCH]",
            headers=self.auth_headers,
            timeout=5
        )
                
    @tag('bands')
    @task(1)
    def remove_band_member(self):
        """Remove a member from a band"""
        if not self.band_ids or not self.artist_ids:
            return
            
        band_id = random.choice(self.band_ids)
        artist_id = random.choice(self.artist_ids)
        
        self.make_request(
            'delete',
            f"{BAND_SERVICE}/api/bands/{band_id}/members/{artist_id}",
            "/api/bands/{id}/members/{id} [DELETE]",
            headers=self.auth_headers,
            timeout=5
        )
    
    # Tour Management Service tasks
    
    @tag('tours')
    @task(1)
    def create_tour(self):
        """Create a tour for a band"""
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        # Generate city visits for the tour
        num_cities = random.randint(3, 6)
        city_visits = []
        
        # Start date for tour (future date)
        current_date = datetime.datetime.now()
        start_date = current_date + datetime.timedelta(days=random.randint(30, 60))
        
        for i in range(num_cities):
            # Each city visit is 1-3 days
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
            timeout=5
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
        """Get tours for a specific band"""
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        response = self.make_request(
            'get',
            f"{TOUR_SERVICE}/api/tours/band/{band_id}",
            "/api/tours/band/{id} [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            # Store tour IDs if we don't have any yet
            if not self.tour_ids and response.json():
                try:
                    for tour in response.json()[:3]:  # Get up to 3 tours
                        if tour.get('id'):
                            self.tour_ids.append(tour['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('tours')
    @task(1)
    def update_tour(self):
        """Update a tour"""
        if not self.tour_ids:
            return
            
        tour_id = random.choice(self.tour_ids)
        
        # Get the tour first to update it properly
        response = self.make_request(
            'get',
            f"{TOUR_SERVICE}/api/tours/{tour_id}",
            "/api/tours/{id} [GET for update]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code != 200:
            return
                
        try:
            tour_data = response.json()
            # Update the tour name
            tour_data["tourName"] = f"Updated Festival Tour {random.randint(1, 100)}"
            
            # Update with the modified data
            self.make_request(
                'put',
                f"{TOUR_SERVICE}/api/tours/{tour_id}",
                "/api/tours/{id} [PUT]",
                json=tour_data,
                headers=self.auth_headers,
                timeout=5
            )
        except (json.JSONDecodeError, AttributeError, KeyError):
            pass
    
    @tag('tours')
    @task(1)
    def add_city_to_tour(self):
        """Add a city visit to a tour"""
        if not self.tour_ids:
            return
            
        tour_id = random.choice(self.tour_ids)
        
        # Create a future date for the city visit
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
            timeout=5
        )
    
    # Music Catalog Service tasks
    
    @tag('albums')
    @task(1)
    def create_album(self):
        """Create an album for a band"""
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        # Create a past date for the album release
        current_date = datetime.datetime.now()
        release_date = current_date - datetime.timedelta(days=random.randint(30, 365))
        
        album_data = {
            "title": random.choice(ALBUM_NAMES) + " " + str(random.randint(1, 100)),
            "releaseDate": release_date.strftime("%Y-%m-%dT%H:%M:%S"),
            "bandId": band_id,
            "songs": []  # We'll add songs in a separate step
        }
        
        response = self.make_request(
            'post',
            f"{MUSIC_SERVICE}/api/albums",
            "/api/albums [POST]",
            json=album_data,
            headers=self.auth_headers,
            timeout=5
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
        """Add a song to the catalog (with or without album)"""
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        # Decide whether to add to an album or as a standalone song
        album_id = random.choice(self.album_ids) if self.album_ids and random.random() > 0.5 else None
        
        song_data = {
            "name": random.choice(SONG_NAMES) + " " + str(random.randint(1, 100)),
            "duration": random.randint(180, 360),  # 3-6 minutes in seconds
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
            timeout=5
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
        """Get songs for a specific band"""
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        response = self.make_request(
            'get',
            f"{MUSIC_SERVICE}/api/songs/band/{band_id}",
            "/api/songs/band/{id} [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            # Store song IDs if we don't have any yet
            if not self.song_ids and response.json():
                try:
                    for song in response.json()[:5]:  # Get up to 5 songs
                        if song.get('id'):
                            self.song_ids.append(song['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass
    
    @tag('albums')
    @task(2)
    def get_albums_by_band(self):
        """Get albums for a specific band"""
        if not self.band_ids:
            return
            
        band_id = random.choice(self.band_ids)
        
        response = self.make_request(
            'get',
            f"{MUSIC_SERVICE}/api/albums/band/{band_id}",
            "/api/albums/band/{id} [GET]",
            headers=self.auth_headers,
            timeout=5
        )
        
        if response.status_code == 200:
            # Store album IDs if we don't have any yet
            if not self.album_ids and response.json():
                try:
                    for album in response.json()[:3]:  # Get up to 3 albums
                        if album.get('id'):
                            self.album_ids.append(album['id'])
                except (json.JSONDecodeError, AttributeError, KeyError):
                    pass