package io.pivotal.web.security;

//@Component
public class CustomCredentialsService { //implements UserDetailsService {

	/**

	private static final Logger logger = LoggerFactory.getLogger(CustomCredentialsService.class);
	
	@Autowired
	private AccountService accountservice;
			
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		logger.info("Looking for user: " + username);
		
		if (username == null) {
			logger.warn("username is null: ");
			throw new UsernameNotFoundException(username);
		}
		
		Account account = accountservice.getAccount(username);
		logger.info("Got account in credentials: " + account);
		UserDetails custom = new CustomDetails(account);
		
		return custom;
	}
	
	public class CustomDetails implements UserDetails {
		private static final String ROLE = "USER"; 
		private Account account;
		
		public CustomDetails(Account account) {
			this.account = account;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE);
			authorities.add(authority); 
			return authorities;
		}

		@Override
		public String getPassword() {
			return account.getPasswd();
		}

		@Override
		public String getUsername() {
			return account.getUserid();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
		
		public String getToken() {
			return account.getAuthtoken();
		}
		
	}
*/

}
