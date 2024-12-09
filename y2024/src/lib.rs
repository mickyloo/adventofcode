use std::io::BufRead;

pub fn start_day(day: &str) {
    println!("Advent of Code 2024 - Day {:0>2}", day);
}

// Additional common functions
pub fn read_to_2d<R: BufRead>(reader: R) -> Vec<Vec<char>> {
    let grid: Vec<Vec<_>> = reader
        .lines()
        .map(|line| line.unwrap().chars().collect::<Vec<_>>())
        .collect();
    grid
}


#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_works() {
        start_day("00");
    }
}
