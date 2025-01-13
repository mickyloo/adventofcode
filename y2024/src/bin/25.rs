use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::{all, iproduct, Itertools};
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "25"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let mut locks: Vec<_> = vec![];
        let mut keys: Vec<_> = vec![];

        let blocks = read_to_blocks(reader)
            .iter()
            .map(|block| block.iter().map(|s| s.chars().collect_vec()).collect_vec())
            .collect_vec();

        for block in blocks {
            let is_lock = block[0].clone().into_iter().join("") == "#####";

            let mut height = [0; 5];
            for i in 0..5 {
                let count = block.iter().filter(|&line| line[i] == '#').count();
                height[i] = count - 1;
            }
            if is_lock {
                locks.push(height);
            } else {
                keys.push(height);
            }
        }

        let answer = iproduct!(locks, keys)
            .map(|(lock, key)| {
                [
                    lock[0] + key[0],
                    lock[1] + key[1],
                    lock[2] + key[2],
                    lock[3] + key[3],
                    lock[4] + key[4],
                ]
            })
            .filter(|&l| all(l.iter(), |x| x <= &5))
            .count();

        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(3, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
